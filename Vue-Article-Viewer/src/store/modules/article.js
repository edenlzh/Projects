import initialArticles from '@/mock/data';
import { getArticles } from '@/utils/getArticle';

// Set initial articles from mock data
let articles = initialArticles;
export default {
    state: {
        // Initialize articles in the state
        articles: getArticles(articles),
    },
    mutations: {
        // Mutation to set articles in the state
        setArticle(state, article) {
            state.articles = article;
        },
    },
    actions: {
        // Action to get and set articles in the state
        async getArticle({ commit }) {
            commit('setArticle', articles);
        },
    },
    getters: {},
    namespaced: true,
};
