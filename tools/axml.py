from pathlib import Path
import struct,zipfile
U32=lambda b,o:struct.unpack_from('<I',b,o)[0]
def chunks(data):
 off=8
 while off<len(data):
  typ,header,size=struct.unpack_from('<HHI',data,off)
  assert size>=header and off+size<=len(data)
  yield typ,data[off:off+size]
  off+=size
def read_pool(b):
 count,stylecount,flags,start,styles=struct.unpack_from('<IIIII',b,8)
 assert stylecount==0,'Manifest string styles need preservation'
 def n8(o):
  n=b[o];return (((n&127)<<8)|b[o+1],o+2) if n&128 else (n,o+1)
 def n16(o):
  n=struct.unpack_from('<H',b,o)[0];return (((n&32767)<<16)|struct.unpack_from('<H',b,o+2)[0],o+4) if n&32768 else (n,o+2)
 out=[]
 for i in range(count):
  off=start+U32(b,28+4*i)
  if flags&256:
   _,off=n8(off);n,off=n8(off);s=b[off:off+n].decode('utf-8')
  else:
   n,off=n16(off);s=b[off:off+n*2].decode('utf-16-le')
  out.append(s)
 return out
def make_pool(strings):
 offsets=[];payload=bytearray()
 def n8(n):return bytes([n]) if n<128 else bytes([(n>>8)|128,n&255])
 for s in strings:
  v=s.encode('utf-8');offsets.append(len(payload))
  payload+=n8(len(s.encode('utf-16-le'))//2)+n8(len(v))+v+b'\0'
 payload+=b'\0'*((-len(payload))%4)
 start=28+4*len(strings)
 return struct.pack('<HHIIIIII',1,28,start+len(payload),len(strings),0,256,start,0)+struct.pack('<'+'I'*len(offsets),*offsets)+payload
