import {SearchApi, SearchApiConfiguration} from '@hitoshura25/mpo-api-search-kmp'

const searchApi = new SearchApi(new SearchApiConfiguration('http://localhost:3306', 300000));

async function init_app() {
    const searchButton = document.getElementById('searchButton');
    searchButton?.addEventListener('click', search);

    const detailsButton = document.getElementById('detailsButton');
    detailsButton?.addEventListener('click', details);
}

async function search() {
    const searchInput = document.getElementById('searchInput') as HTMLInputElement;
    const query = searchInput?.value;
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

async function details() {
    const feedUrl = (document.getElementById('feedUrlInput') as HTMLInputElement)?.value ;
    const episodesOffset = (document.getElementById('episodesOffsetInput') as HTMLInputElement)?.value;
    const episodesLimit = (document.getElementById('episodesLimitInput') as HTMLInputElement)?.value;
    
    const podcastDetails = await searchApi.podcastDetails(feedUrl, Number(episodesOffset), Number(episodesLimit));
    const detailsDiv = document.getElementById('detailsResults') as HTMLDivElement;
    if (detailsDiv) {
        detailsDiv.innerHTML = ''; // Clear previous results

        // Display podcast details
        const episodes = podcastDetails.episodes.asJsReadonlyArrayView();
        const podcastDiv = document.createElement('div');
        podcastDiv.innerHTML = `
            <h2>${podcastDetails.name}</h2>
            <p>${podcastDetails.description}</p>
            <img src="${podcastDetails.imageUrl}" alt="Podcast Image">
            <p>Total Episodes: ${episodes.length}</p>
        `;
        detailsDiv.appendChild(podcastDiv);

        // Optionally, display episode list
        const episodesList = document.createElement('ul');
        episodes.forEach(episode => {
            const episodeItem = document.createElement('li');
            episodeItem.innerHTML = `
                <h3>${episode.name}</h3>
                <p>${episode.description}</p>
                <audio controls src="${episode.downloadUrl}"></audio>
            `;
            episodesList.appendChild(episodeItem);
        });
        detailsDiv.appendChild(episodesList);
    }
}

window.addEventListener('DOMContentLoaded', init_app);