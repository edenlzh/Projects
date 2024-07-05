// router/index.js
// Import necessary modules from Vue Router and Vuex store
import { createRouter, createWebHistory } from 'vue-router';
import store from '@/store';

// Define the routes for the application
const routes = [
    // Redirect the root path to the first article
    {
        path: '/',
        redirect: '/articles/1',
    },
    // Catch-all route to redirect to 404 page
    {
        path: '/:pathMatch(.*)',
        redirect: '/404',
    },
    // 404 page route
    {
        path: '/404',
        name: '/404',
        component: () => import('@/views/404.vue'),
    },
    // Route for individual articles
    {
        path: '/articles/:id',
        component: () => import('@/views/Article/ArticlePage.vue'),
        children: [
            {
                path: '',
                name: 'articles',
                component: () => import('@/views/Article/components/Page'),
            },
            {
                path: '/addArticle',
                name: 'addArticle',
                component: () => import('@/views/AddArticle'),
            },
        ],
    },
    // Route for the gallery
    {
        path: '/gallery',
        name: 'gallery',
        component: () => import('@/views/Gallery'),
    },
];

// Create the router instance
const router = createRouter({
    mode: 'history',
    history: createWebHistory(),
    routes,
});

// Define the whiteList for route names
const whiteList = ['articles', 'addArticle', 'gallery'];

// Route guard to check the route name against the whiteList
router.beforeEach((to, _, next) => {
    store.state.article.articles;
    if (whiteList.includes(to.name)) {
        next();
    } else next();
});

// Export the router instance
export default router;
