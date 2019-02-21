import {makeRequest, METHODS} from "@entando/apimanager";
import {PAGE_CONFIGURATION, LANGUAGES} from "mocks/appBuilder";

export const getPageConfiguration = pageCode =>
  makeRequest({
    uri: `/api/pages/${pageCode}/configuration`,
    method: METHODS.GET,
    mockResponse: PAGE_CONFIGURATION || {},
    useAuthentication: true
  });

export const getLanguages = () =>
  makeRequest({
    uri: `/api/languages`,
    method: METHODS.GET,
    mockResponse: LANGUAGES || [],
    useAuthentication: false
  });
