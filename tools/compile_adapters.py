from pathlib import Path
import argparse,subprocess
p=argparse.ArgumentParser();p.add_argument('--android-jar',type=Path,required=True);p.add_argument('--javac',default='javac');p.add_argument('--output',type=Path,required=True);a=p.parse_args()
r=Path(__file__).resolve().parent.parent
for group in ('aircommand','capture','magnify'):
 dst=a.output/group;dst.mkdir(parents=True,exist_ok=True)
 sources=sorted((r/'adapters'/group).rglob('*.java'))
 subprocess.run([a.javac,'--release','8','-encoding','UTF-8','-cp',str(a.android_jar),'-d',str(dst),*map(str,sources)],check=True)
 print(group,len(sources),'sources compiled')
