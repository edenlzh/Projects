export function getArticles(articles) {
    let items = localStorage.getItem('articles') ?? [];
    if (items.length > 0) {
        return articles.concat(JSON.parse(items));
    }
    return articles;
}
