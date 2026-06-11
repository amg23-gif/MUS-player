const CACHE='mus-v5';
  const SHELL=['/MUS-player/','/MUS-player/index.html','/MUS-player/manifest.json'];
  self.addEventListener('install',e=>{
    e.waitUntil(caches.open(CACHE).then(c=>c.addAll(SHELL)).then(()=>self.skipWaiting()));
  });
  self.addEventListener('activate',e=>{
    e.waitUntil(caches.keys().then(ks=>Promise.all(ks.filter(k=>k!==CACHE).map(k=>caches.delete(k)))).then(()=>self.clients.claim()));
  });
  self.addEventListener('fetch',e=>{
    if(e.request.method!=='GET')return;
    const url=new URL(e.request.url);
    if(url.pathname.endsWith('.m3u8')||url.pathname.endsWith('.ts')||url.pathname.endsWith('.m3u'))return;
    e.respondWith(
      fetch(e.request).then(res=>{
        if(res&&res.status===200&&res.type==='basic'){
          const clone=res.clone();
          caches.open(CACHE).then(c=>c.put(e.request,clone));
        }
        return res;
      }).catch(()=>caches.match(e.request).then(c=>c||caches.match('/MUS-player/')))
    );
  });
  