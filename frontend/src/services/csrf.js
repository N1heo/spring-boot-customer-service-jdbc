import axios from "axios";

function getCookie(name) {
  const value = `; ${document.cookie}`;
  const parts = value.split(`; ${name}=`);
  if (parts.length === 2) return parts.pop().split(";").shift();
  return null;
}

let inFlight = null;

export function initCsrf() {

  if (getCookie("XSRF-TOKEN")) return Promise.resolve();


  if (!inFlight) {
    inFlight = axios.get("/api/csrf").catch(() => {}).finally(() => {
      inFlight = null;
    });
  }
  return inFlight;
}
