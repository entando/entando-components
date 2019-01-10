import {makeMockRequest, makeRequest, METHODS} from "@entando/apimanager";
import {PAGE_CONFIGURATION, LANGUAGES} from "mocks/appBuilder";

export const getPageConfiguration = pageCode =>
  makeMockRequest({
    uri: `/api/pages/${pageCode}/configuration`,
    method: METHODS.GET,
    mockResponse: PAGE_CONFIGURATION,
    useAuthentication: false
  });

export const getLanguages = () =>
  makeMockRequest({
    uri: `/api/languages`,
    method: METHODS.GET,
    mockResponse: LANGUAGES,
    useAuthentication: false
  });
