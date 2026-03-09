export const BASE_URL = "http://localhost:8080/api";

export const API_ENDPOINTS = {
  LOGIN: "/login",
  REGISTER: "/register",

  // gigs
  GIGS: "/gigs",                // create, update, delete
  MY_GIGS: "/gigs/my",          // active gigs
  MY_ALL_GIGS: "/gigs/my/all",  // active + inactive gigs
  RESTORE_GIG: "/gigs/restore"  // restore endpoint
};