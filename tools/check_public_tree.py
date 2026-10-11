from pathlib import Path
import re,subprocess,hashlib,json
r=Path(__file__).resolve().parent.parent
p=subprocess.run(['git','ls-files','-z'],cwd=r,capture_output=True,check=True)
files=[r/n.decode() for n in p.stdout.split(b'\0') if n];bad=[]
blocked={'.apk','.aab','.dex','.class','.jar','.so','.zip','.tar','.p12','.jks','.keystore','.pem','.key','.smali','.png','.jpg','.wav'}
patterns=[r'gh[pousr]_[A-Za-z0-9]{20,}',r'github_pat_[A-Za-z0-9_]{20,}',r'-----BEGIN (?:RSA |EC |OPENSSH )?PRIVATE KEY-----',r'(?i)[a-z]:\\users\\',r'(?i)(?:[0-9a-f]{2}:){5}[0-9a-f]{2}']
for f in files:
 if f.suffix.lower() in blocked or f.name=='password.txt':bad.append(str(f.relative_to(r)));continue
 text=f.read_text(encoding='utf8')
 if any(re.search(x,text) for x in patterns):bad.append(str(f.relative_to(r)))
assert not bad,'Excluded or sensitive content: '+', '.join(bad)
print(len(files),'tracked text files checked; no blocked payloads or detected secrets')
