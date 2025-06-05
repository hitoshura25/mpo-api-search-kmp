import {SearchApi, SearchApiConfiguration} from '@hitoshura25/mpo-api-search-kmp'

async function init_app() {
  const searchButton = document.getElementById('searchButton');
  searchButton?.addEventListener('click', search);
}

async function search() {
    const searchInput = document.getElementById('searchInput') as HTMLInputElement;
    const query = searchInput?.value;
    const searchApi = new SearchApi(new SearchApiConfiguration('http://localhost:8080'));
    try {
        const results = await searchApi.searchPodcasts(query);
        const resultsDiv = document.getElementById('results') as HTMLDivElement;
        if (resultsDiv) {
            resultsDiv.innerHTML = '';
        }

        results.forEach(result => {
            const div = document.createElement('div');
            div.className = 'result';
            div.innerHTML = `
                <h3>${result.name}</h3>
                ${result.artworkUrl ? `<img src="${result.artworkUrl}" alt="Podcast artwork">` : ''}
                <p>Author: ${result.author}</p>
                <p>Genres: ${result.genres.asJsReadonlyArrayView().join(', ')}</p>
                <p>Feed URL: ${result.feedUrl}</p>
            `;
            if (resultsDiv) {
                resultsDiv.appendChild(div);
            }
        });
    } catch (error) {
        console.error('Search failed:', error);
    }
}

window.addEventListener('DOMContentLoaded', init_app);