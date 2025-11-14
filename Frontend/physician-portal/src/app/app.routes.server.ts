import { RenderMode, ServerRoute } from '@angular/ssr';
// NotificationService import and usage removed if present

export const serverRoutes: ServerRoute[] = [
  {
    path: '**',
    renderMode: RenderMode.Prerender
  }
];
