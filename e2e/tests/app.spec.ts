import { test, expect } from '@playwright/test';
import { AddressInfo } from 'net';
import { setupMockServer } from './mocks/mock-server';

test.describe('Podcast Search App', () => {
  let mockServer: any;
  let mockServerPort: number;

  test.beforeAll(async () => {
    mockServer = await setupMockServer({
      port: 3306
    });
    mockServerPort = (mockServer.address() as AddressInfo).port;
  });

  test.afterAll(async () => {
    mockServer.close();
  });
  test('should perform search when entering text and clicking search', async ({ page }) => {
      await page.goto('http://localhost:8080', { waitUntil: 'networkidle' });

      // Enter search term
      const searchInput = page.locator('#searchInput');
      const searchButton = page.locator('#searchButton');
      expect(searchButton).toBeVisible({ timeout: 10000 })
      await searchInput.fill('test podcast');

      // Click search button
      await searchButton.click();

      // Wait for results
      await expect(page.locator('#results')).toBeVisible({ timeout: 15000 });

      // Verify results are loaded
      const resultElements = page.locator('.result');

      // Verify result structure
      const firstResult = resultElements.first();
      await expect(firstResult.locator('h3')).toHaveText(/Game Scoop/, { timeout: 1000 }); // Any text in h3

      const elements = firstResult.locator('p')
      await expect(elements.first()).toHaveText(/Author: IGN/, { timeout: 1000 }); // Any text in h3
      await expect(elements.nth(1)).toHaveText(/Genres: Video Games/, { timeout: 1000 }); // Any text in h3
      await expect(elements.nth(2)).toHaveText(/Feed URL/, { timeout: 1000 }); // Any text in h3
    });
});