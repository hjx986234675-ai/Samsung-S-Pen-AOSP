from pathlib import Path
import argparse, struct, zipfile
from axml import chunks, read_pool, make_pool, U32

p = argparse.ArgumentParser(description="Declare AOSP hidden API usage for the system-mounted Air Command only; output requires re-signing")
p.add_argument("source", type=Path)
p.add_argument("output", type=Path)
a = p.parse_args()
with zipfile.ZipFile(a.source) as z:
    original = z.read("AndroidManifest.xml")
parts = list(chunks(original))
strings = read_pool(next(b for t,b in parts if t == 1))
def ix(s):
    if s not in strings: strings.append(s)
    return strings.index(s)
android = ix("http://schemas.android.com/apk/res/android")
name = ix("usesNonSdkApi")
b = next(b for t,b in parts if t == 0x0180)
ids = list(struct.unpack("<" + "I" * ((len(b)-8)//4), b[8:]))
ids.extend([0] * max(0, name + 1 - len(ids)))
assert ids[name] in (0, 0x0101058e)
ids[name] = 0x0101058e
new = []
found = False
for t,b in parts:
    if t == 0x0102 and strings[U32(b,20)] == "application":
        count = struct.unpack_from("<H", b, 28)[0]
        offset = 16 + struct.unpack_from("<H", b, 24)[0]
        assert offset == 36 and len(b) == offset + count * 20
        attrs = [b[offset+20*i:offset+20*(i+1)] for i in range(count)]
        attrs = [v for v in attrs if U32(v,4) != name]
        attrs.append(struct.pack("<IIIHBBI", android, name, 0xffffffff, 8, 0, 0x12, 0xffffffff))
        attrs.sort(key=lambda v: ids[U32(v,4)] if U32(v,4) < len(ids) else 0)
        header = bytearray(b[:offset])
        struct.pack_into("<I", header, 4, offset+20*len(attrs))
        struct.pack_into("<H", header, 28, len(attrs))
        b = bytes(header) + b"".join(attrs)
        found = True
    new.append((t,b))
assert found
body = b"".join(make_pool(strings) if t == 1 else
    struct.pack("<HHI", 0x0180, 8, 8+4*len(ids)) + struct.pack("<"+"I"*len(ids), *ids)
    if t == 0x0180 else b for t,b in new)
manifest = struct.pack("<HHI", 3, 8, 8+len(body)) + body
with zipfile.ZipFile(a.source) as z, zipfile.ZipFile(a.output, "w") as target:
    for info in z.infolist():
        if not info.filename.startswith("META-INF/"):
            target.writestr(info, manifest if info.filename == "AndroidManifest.xml" else z.read(info.filename))
print("Application-only usesNonSdkApi declaration added; no global policy changes")
