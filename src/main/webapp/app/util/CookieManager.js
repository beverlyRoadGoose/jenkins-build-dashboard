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

/* cookie manager */

export default {

  /**
   * Creates a cookie from the given parameters
   *
   * @param name Identifier for the cookie
   * @param value Value stored in the cookie
   * @param days Validity of the cookie in days
   */
  createCookie: function (name, value, days) {
    let expires = "";

    if (days) {
      let date = new Date();
      date.setTime(date.getTime() + (days * 24 * 60 * 60 * 1000));
      expires = "; expires=" + date.toUTCString();
    }

    document.cookie = name + "=" + value + expires + "; path=/";
  },

  /**
   * Reads the value of a cookie
   *
   * @param name Identifier of the cookie
   * @returns The value stored in the cookie
   */
  readCookie: function (name) {
    let nameEQ = name + "=";
    let ca = document.cookie.split(';');

    for(let i=0; i < ca.length; i++) {
      let c = ca[i];
      while (c.charAt(0) === ' ') c = c.substring(1,c.length);
      if (c.indexOf(nameEQ) === 0) return c.substring(nameEQ.length,c.length);
    }

    return null;
  },

  /**
   * Deletes a cookie.
   *
   * @param name Identifier of the cookie to be deleted
   */
  deleteCookie: function (name) {
    this.createCookie(name,"",-1);
  },

  /**
   * Check if a cookie exists
   * @param name The identifier of the cookie to check for
   * @returns true of the cookie exists
   */
  cookieExists: function (name) {
    return this.readCookie(name) !== null;
  }

}