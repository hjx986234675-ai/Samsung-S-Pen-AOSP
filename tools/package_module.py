from pathlib import Path
import argparse,zipfile,hashlib,json
p=argparse.ArgumentParser(description='Local-only packaging of already adapted, same-signed APKs; no Samsung redistribution rights granted')
for name in ('air','capture','magnify','output'):p.add_argument('--'+name,type=Path,required=True)
a=p.parse_args();r=Path(__file__).resolve().parent.parent
rows=[];a.output.parent.mkdir(parents=True,exist_ok=True)
with zipfile.ZipFile(a.output,'w',compression=zipfile.ZIP_DEFLATED) as z:
 for f in sorted((r/'module-template').rglob('*')):
  if f.is_file():z.write(f,f.relative_to(r/'module-template').as_posix())
 for name,f in [('SamsungAirCommand',a.air),('SamsungSmartCapture',a.capture),('SamsungMagnifier',a.magnify)]:
  if not zipfile.is_zipfile(f):raise ValueError('Input is not an APK ZIP')
  target='system/app/'+name+'/'+name+'.apk';z.write(f,target)
  rows.append({'path':target,'sha256':hashlib.sha256(f.read_bytes()).hexdigest()})
 z.writestr('payload-sha256.json',json.dumps(rows,indent=2));z.write(r/'docs/PERMISSIONS.md','README.md')
print('Local module written. Input signing and Samsung compatibility must be verified separately.')
