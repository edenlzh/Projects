import { createStore } from 'vuex';
import article from './modules/article';
// Create and expose store
export default createStore({
    modules: {
        article,
    },
});
