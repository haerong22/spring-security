### Spring Security Basic

---

#### Spring Security Basic API & Filter
- Form Login
  - Form Login Authentication Filter (UsernamePasswordAuthenticationFilter)
  - Logout (LogoutFilter)
  

- Remember Me
  - RememberMeAuthenticationFilter
  

- Anonymous User
  - AnonymousAuthenticationFilter


- Session Management
  - concurrentSession
  - sessionFixation
  - sessionCreationPolicy
  

- Authorization


- Exception Handling
  - ExceptionTranslationFilter
    - FilterSecurityInterceptor
    - AccessDeniedHandler
    - AuthenticationEntryPoint 


- CSRF
  - CsrfFilter
  
  
#### Spring security Architecture

- Proxy
  - DelegatingProxyChain
  - FilterChainProxy
  

- Multiple Configuration


- Authentication
  - SecurityContextHolder
    - SecurityContext
      - Authentication
        - Principal
        - Credentials
        - Authorities
        - Details
        - Authenticated
    - SecurityContextPersistenceFilter
  - AuthenticationManager
  - AuthenticationProvider
  

- Authorization
  - FilterSecurityInterceptor
    - SecurityMetadataSource
    - AccessDecisionManager
      - AffirmativeBased
      - ConsensusBased
      - UnanimousBased
    - AccessDecisionVoter
      