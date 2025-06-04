import { test, expect } from '@playwright/test';

test.describe('Podcast Search App', () => {
  test('should perform search when entering text and clicking search', async ({ page }) => {
      await page.goto('http://localhost:8080');

      // Enter search term
      await page.locator('#searchInput').fill('test podcast');

      // Click search button
      await page.locator('#searchButton').click();

      // Wait for results
      await expect(page.locator('#results')).toBeVisible();

      // Verify results are loaded
      const resultElements = page.locator('.result');
      //await expect(resultElements).toHaveCount().above(0);

      // Verify result structure
      const firstResult = resultElements.first();
      await expect(firstResult.locator('h3')).toHaveText(/Game Scoop/, { timeout: 1000 }); // Any text in h3

      const elements = firstResult.locator('p')
      await expect(elements.first()).toHaveText(/Author: IGN/, { timeout: 1000 }); // Any text in h3
      await expect(elements.nth(1)).toHaveText(/Genres: Video Games/, { timeout: 1000 }); // Any text in h3
      await expect(elements.nth(2)).toHaveText(/Feed URL/, { timeout: 1000 }); // Any text in h3
    });
});