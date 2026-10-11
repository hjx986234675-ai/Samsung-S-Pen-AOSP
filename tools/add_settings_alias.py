from pathlib import Path
import struct,zipfile,json,hashlib,shutil
import argparse
p=argparse.ArgumentParser(description="Add AOSP Settings alias to an authorized local Air Command APK; output must be re-signed and include the resource package")
p.add_argument("source",type=Path);p.add_argument("output",type=Path);args=p.parse_args()
from axml import chunks,read_pool,make_pool,U32
source=args.source
with zipfile.ZipFile(source) as z:original=z.read('AndroidManifest.xml')
parts=list(chunks(original));strings=read_pool(next(b for t,b in parts if t==1));idx={s:i for i,s in enumerate(strings)}
def ix(s):
 if s not in idx:idx[s]=len(strings);strings.append(s)
 return idx[s]
android=ix('http://schemas.android.com/apk/res/android')
resource_ids={'name':0x01010003,'label':0x01010001,'permission':0x01010006,'enabled':0x0101000e,'exported':0x01010010,'targetActivity':0x01010202,'value':0x01010024,'resource':0x01010025,'icon':0x01010002}
def start(tag,attrs=()):
 attrs=sorted(attrs,key=lambda a:resource_ids[a[0]]);raw=[]
 for key,value in attrs:
  n=ix(key)
  if isinstance(value,int) and not isinstance(value,bool):raw.append(struct.pack('<IIIHBBI',android,n,0xffffffff,8,0,1,value))
  elif isinstance(value,bool):raw.append(struct.pack('<IIIHBBI',android,n,0xffffffff,8,0,0x12,0xffffffff if value else 0))
  else:raw.append(struct.pack('<IIIHBBI',android,n,ix(value),8,0,3,ix(value)))
 a=b''.join(raw)
 return struct.pack('<HHIII',0x0102,16,36+len(a),0,0xffffffff)+struct.pack('<IIHHHHHH',0xffffffff,ix(tag),20,20,len(attrs),0,0,0)+a
def end(tag):return struct.pack('<HHIII',0x0103,16,24,0,0xffffffff)+struct.pack('<II',0xffffffff,ix(tag))
assert 'com.samsung.android.service.aircommand.settings.AospSpenSettings' not in strings,'Settings alias already present; use the pre-integration input'
alias='com.samsung.android.service.aircommand.settings.AospSpenSettings'
addition=[start('activity-alias',[('name',alias),('targetActivity','com.samsung.android.service.aircommand.ui.settings.spen.SpenSettingsActivity'),('label','S Pen'),('icon',0x7e020000),('permission',''),('enabled',True),('exported',True)]),start('intent-filter'),start('action',[('name','com.android.settings.action.EXTRA_SETTINGS')]),end('action'),start('action',[('name','com.android.settings.action.IA_SETTINGS')]),end('action'),start('category',[('name','android.intent.category.DEFAULT')]),end('category'),end('intent-filter')]
for key,value in [('com.android.settings.category','com.android.settings.category.ia.homepage'),('com.android.settings.keyhint','samsung_spen_settings'),('com.android.settings.title','S Pen'),('com.android.settings.summary','Air actions, Air command and S Pen settings')]:addition += [start('meta-data',[('name',key),('value',value)]),end('meta-data')]
addition += [start('meta-data',[('name','com.android.settings.icon'),('resource',0x7e020000)]),end('meta-data'),start('meta-data',[('name','com.android.settings.icon_tintable'),('value',True)]),end('meta-data'),start('meta-data',[('name','com.android.settings.bg.hint'),('resource',0x7e030000)]),end('meta-data')]
addition += [start('intent-filter'),start('action',[('name','android.intent.action.MAIN')]),end('action'),start('category',[('name','android.intent.category.LAUNCHER')]),end('category'),end('intent-filter'),end('activity-alias')]
new=[]
for t,b in parts:
 if t==0x0103 and strings[U32(b,20)]=='application':new.extend((0x0102 if U32(x,0)&0xffff==0x0102 else 0x0103,x) for x in addition)
 new.append((t,b))
b=next(b for t,b in parts if t==0x0180);ids=list(struct.unpack('<'+'I'*((len(b)-8)//4),b[8:]))
for name,id in resource_ids.items():
 i=ix(name)
 if i>=len(ids):ids.extend([0]*(i-len(ids)+1))
 assert ids[i] in (0,id);ids[i]=id
body=b''.join(make_pool(strings) if t==1 else struct.pack('<HHI',0x0180,8,8+4*len(ids))+struct.pack('<'+'I'*len(ids),*ids) if t==0x0180 else b for t,b in new)
manifest=struct.pack('<HHI',3,8,8+len(body))+body
out=args.output
with zipfile.ZipFile(source) as z,zipfile.ZipFile(out,'w') as dst:
 for info in z.infolist():
  if not info.filename.startswith('META-INF/'):dst.writestr(info,manifest if info.filename=='AndroidManifest.xml' else z.read(info.filename))
with zipfile.ZipFile(source) as z,zipfile.ZipFile(out) as z2:
 assert all(z.read(n)==z2.read(n) for n in z.namelist() if n!='AndroidManifest.xml' and not n.startswith('META-INF/'))
print('Only manifest changed: original settings alias exposed through AOSP settings tile and launcher; DEX/UI/actions unchanged')
