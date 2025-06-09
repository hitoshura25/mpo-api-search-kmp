import express from 'express';
import jwt from 'jsonwebtoken';
import cors from 'cors';

export async function setupMockServer(config) {
  const app = express();
  app.use(cors());
  app.use(express.urlencoded({ extended: true }));
  app.use(express.json());

  app.get('/search', (req, res) => {
    res.json(
        {
            resultCount: 1,
            results: [
                {
                    name: "Game Scoop!",
                    author: "IGN & Geek Media",
                    feedUrl: "https://rss.pdrl.fm/817ebc/feeds.megaphone.fm/gamescoop",
                    genres: [
                        "Video Games",
                        "Podcasts",
                        "Leisure",
                        "Games"
                    ],
                    artworkUrl: "https://is1-ssl.mzstatic.com/image/thumb/Features4/v4/d6/d5/98/d6d59873-2ee5-1f88-1e9d-224cc4a67b53/mza_1229187100463163224.jpg/600x600bb.jpg"
                }
            ]
        }
     );
  });

  app.get('/details', (req, res) => {
    res.json(
      {
        name: "Test Podcast",
        description: "Test Description",
        imageUrl: "https://example.com/default.jpg",
        episodes: [
          {
            "name": "Complete Episode",
            "description": "Full description",
            "published": "2025-06-08T15:30:00",
            "downloadUrl": "https://example.com/episode1.mp3",
            "artworkUrl": "https://example.com/thumb1.jpg",
            "durationInSeconds": "5445",
            "type": "audio/mpeg"
          }
        ],
        pagination: {
            total: 4,
            limit: 1,
            offset: 1,
            next_page: "/details/?feed_url=https://rss.pdrl.fm/817ebc/feeds.megaphone.fm/gamescoop&episode_limit=1&episode_offset=2",
            previous_page: "/details/?feed_url=https://rss.pdrl.fm/817ebc/feeds.megaphone.fm/gamescoop&episode_limit=1&episode_offset=0"
          }
      }
     );
  });

  const server = app.listen(config.port || 0, () => {
    console.log(`Mock search server listening on port ${server.address().port}`);
  });

  return server;
}