package org.bitbucket.shevchenkod.restaurant.test.controller;

import com.fasterxml.jackson.databind.JsonNode;
import org.bitbucket.shevchenkod.restaurant.util.JsonUtils;

import org.springframework.http.MediaType;
import org.springframework.security.authentication.TestingAuthenticationToken;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMultipartHttpServletRequestBuilder;
import org.springframework.test.web.servlet.setup.StandaloneMockMvcBuilder;

import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup;

public abstract class AbstractControllerTestCase<C> extends AbstractTransactionalJUnit4SpringContextTests {

    protected abstract C getController();

    protected MockMvc mvcSetup() {
        return standaloneSetup(getController()).build();
    }


    protected MvcResult performAuthenticatedGetRequestAndReturn(Authentication authentication,Object controller, String url, String... params) {
        try {
            return performAuthenticatedRequestAndReturn(authentication, controller, getRequest(url, params));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    protected MvcResult performAuthenticatedGetRequestAndReturn(Authentication authentication, String url, String... params) {
        try {
            return performAuthenticatedRequestAndReturn(authentication, getController(), getRequest(url, params));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    protected MvcResult performAuthenticatedPostRequestAndReturn(Authentication authentication, Object controller, String url, String... params) {
        try {
            return performAuthenticatedRequestAndReturn(authentication, controller, postRequest(url, params));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    protected MvcResult performAuthenticatedPostRequest(Authentication authentication, String url, String... params) {
        try {
            return performAuthenticatedRequestAndReturn(authentication, getController(), postRequest(url, params));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    protected MvcResult performAuthenticatedDeleteRequestAndReturn(Authentication authentication, String url, String... params) {
        try {
            return performAuthenticatedRequestAndReturn(authentication, getController(), deleteRequest(url, params));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
	
	protected MvcResult performAuthenticatedGetRequestAndReturn(Authentication authentication,Object controller, String url) {
        try {
            return performAuthenticatedRequestAndReturn(authentication, controller, getRequest(url));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    protected MvcResult performAuthenticatedGetRequestAndReturn(Authentication authentication, String url) {
        try {
            return performAuthenticatedRequestAndReturn(authentication, getController(), getRequest(url));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    protected MvcResult performAuthenticatedPostRequestAndReturn(Authentication authentication,Object controller, String url) {
        try {
            return performAuthenticatedRequestAndReturn(authentication, controller, postRequest(url));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    protected MvcResult performAuthenticatedPostRequest(Authentication authentication, String url) {
        try {
            return performAuthenticatedRequestAndReturn(authentication, getController(), postRequest(url));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    protected MvcResult performAuthenticatedDeleteRequestAndReturn(Authentication authentication, String url) {
        try {
            return performAuthenticatedRequestAndReturn(authentication, getController(), deleteRequest(url));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    protected MvcResult performAuthenticatedRequestAndReturn(Authentication authentication, MockHttpServletRequestBuilder request) {
        return performAuthenticatedRequestAndReturn(authentication, getController(), request);
    }

    protected MvcResult performAuthenticatedRequestAndReturn(Authentication authentication, Object controller, MockHttpServletRequestBuilder request) {
        try {
			return performAuthenticatedRequest(authentication, controller, request).andExpect(status().isOk()).andReturn();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

	protected JsonNode performAuthenticatedRequestAndReturnJsonNode(Authentication authentication, MockHttpServletRequestBuilder request) {
		return performAuthenticatedRequestAndReturnJsonNode(authentication, getController(), request);
	}

	protected JsonNode performAuthenticatedRequestAndReturnJsonNode(Authentication authentication, Object controller, MockHttpServletRequestBuilder request) {
        try {
			return contentAsJson(performAuthenticatedRequest(authentication, controller, request.accept(MediaType.APPLICATION_JSON)).andExpect(status().isOk()).andReturn().getResponse().getContentAsString());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

	protected ResultActions performAuthenticatedRequest(Authentication authentication, MockHttpServletRequestBuilder request) {
		return performAuthenticatedRequest(authentication, getController(), request);
	}

	protected ResultActions performAuthenticatedRequest(Authentication authentication, Object controller, MockHttpServletRequestBuilder request) {
		try {
			StandaloneMockMvcBuilder setup = standaloneSetup(controller);
			SecurityContextHolder.getContext().setAuthentication(authentication);
			ResultActions reslt = setup.build().perform(addAuthentication(authentication, request));
			SecurityContextHolder.clearContext();
			return reslt;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}


	protected MockHttpServletRequestBuilder getRequest(String url, String... params) {
        MockHttpServletRequestBuilder request = processParams(get(url), params);
        return request;
    }

    protected MockHttpServletRequestBuilder postRequest(String url, String... params) {
        MockHttpServletRequestBuilder request = processParams(post(url), params);
        return request;
    }

    protected MockHttpServletRequestBuilder deleteRequest(String url, String... params) {
        MockHttpServletRequestBuilder delete = delete(url);
        MockHttpServletRequestBuilder request = processParams(delete, params);
        return request;
    }

    private MockHttpServletRequestBuilder processParams(MockHttpServletRequestBuilder delete, String[] params) {
        MockHttpServletRequestBuilder request = delete;//.accept(MediaType.APPLICATION_JSON);

        String paramKey = null;
        for (String param : params) {
            if (paramKey == null) {
                paramKey = param;
            } else {
                request.param(paramKey, param);
                paramKey = null;
            }
        }
        return request;
    }



    public JsonNode contentAsJson(String content) {
        return JsonUtils.parse(content);
    }

    public MockHttpServletRequestBuilder restApiPostAuthenticatedRequest(UserDetails user, String url) {
        return addAuthentication(user, post(url));
    }

    public MockMultipartHttpServletRequestBuilder restApiFilePostAuthenticatedRequest(UserDetails user, String url) {
        return addAuthentication(user, fileUpload(url));
    }

    public MockHttpServletRequestBuilder restApiPutAuthenticatedRequest(UserDetails user, String url) {
        return addAuthentication(user, put(url));
    }

    public MockHttpServletRequestBuilder restApiDeleteAuthenticatedRequest(UserDetails user, String url) {
        return addAuthentication(user, delete(url));
    }

    public MockHttpServletRequestBuilder restApiGetAuthenticatedRequest(UserDetails user, String url) {
        return addAuthentication(user, get(url));
    }
	
    public MockHttpServletRequestBuilder restApiPostAuthenticatedRequest(Authentication authentication, String url) {
        return addAuthentication(authentication, post(url));
    }

    public MockMultipartHttpServletRequestBuilder restApiFilePostAuthenticatedRequest(Authentication authentication, String url) {
        return addAuthentication(authentication, fileUpload(url));
    }

    public MockHttpServletRequestBuilder restApiPutAuthenticatedRequest(Authentication authentication, String url) {
        return addAuthentication(authentication, put(url));
    }

    public MockHttpServletRequestBuilder restApiDeleteAuthenticatedRequest(Authentication authentication, String url) {
        return addAuthentication(authentication, delete(url));
    }

    public MockHttpServletRequestBuilder restApiGetAuthenticatedRequest(Authentication authentication, String url) {
        return addAuthentication(authentication, get(url));
    }

	private <T extends MockHttpServletRequestBuilder> T addAuthentication(UserDetails user, T mockHttpServletRequestBuilder) {
		return addAuthentication(
				//		new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword(), user.getAuthorities()),
				new TestingAuthenticationToken(user.getUsername(), user.getPassword(), (List<GrantedAuthority>) user.getAuthorities()),
				mockHttpServletRequestBuilder
		);
	}

	private <T extends MockHttpServletRequestBuilder> T addAuthentication(Authentication authentication, T mockHttpServletRequestBuilder) {
		T result = (T) mockHttpServletRequestBuilder
				//.accept(MediaType.APPLICATION_JSON)
				.principal(authentication);
		return result;
	}
}