/*
 * MIT License
 *
 * Copyright (c) 2019 Oluwatobi Adeyinka
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 *
 */

/* settings manager */

import CookieManager from './CookieManager';

const ANALYTICS_SUFFIX = '_analytics';
const COLUMN_COUNT_COOKIE_SUFFIX = '_columnCount';

export default {

  allowsAnalyticsTracking: function (boardName) {
    let cookieName = boardName.split(' ').join('_') + ANALYTICS_SUFFIX;
    return CookieManager.readCookie(cookieName) === 'true';
  },

  setAnalyticsTracking: function (boardName, value) {
    let cookieName = boardName.split(' ').join('_') + ANALYTICS_SUFFIX;
    CookieManager.deleteCookie(cookieName);
    CookieManager.createCookie(cookieName, value, 365);
  },

  getColumnCount: function (boardName) {
    let cookieName = boardName.split(' ').join('_') + COLUMN_COUNT_COOKIE_SUFFIX;
    let count = CookieManager.readCookie(cookieName);
    return count === null ? 1 : count;
  },

  saveColumnCount: function (boardName, count) {
    let cookieName = boardName.split(' ').join('_') + COLUMN_COUNT_COOKIE_SUFFIX;
    CookieManager.deleteCookie(cookieName);
    CookieManager.createCookie(cookieName, count, 365);
  }

}