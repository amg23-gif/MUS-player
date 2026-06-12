// Service Worker معطّل — يمنع أي كاش قديم
self.addEventListener('install', () => self.skipWaiting());
self.addEventListener('activate', e => {
  e.waitUntil(
    caches.keys().then(keys => Promise.all(keys.map(k => caches.delete(k))))
      .then(() => self.clients.claim())
  );
});
// لا كاش — كل الطلبات تذهب للشبكة مباشرة
