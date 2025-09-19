# Auth0 Integration Setup Guide

This guide will help you configure Auth0 for JWT token verification in your Spring Boot application.

## Prerequisites (Done)

1. An Auth0 account (sign up at https://auth0.com)
2. A configured Auth0 application

## Auth0 Configuration Steps

### 1. Create an Auth0 API (Done)

1. Log in to your Auth0 Dashboard
2. Go to **Applications** > **APIs**
3. Click **Create API**
4. Fill in the details:
   - **Name**: TMS API (or your preferred name)
   - **Identifier**: `https://your-api-identifier` (this will be your audience)
   - **Signing Algorithm**: RS256
5. Click **Create**

### 2. Configure Your Auth0 Application (Done)

1. Go to **Applications** > **Applications**
2. Create a new application or use an existing one
3. Choose **Single Page Application** or **Regular Web Application** based on your frontend
4. In the application settings:
   - Add your frontend URL to **Allowed Callback URLs**
   - Add your frontend URL to **Allowed Web Origins**
   - Add your frontend URL to **Allowed Logout URLs**

### 3. Set Up Custom Claims (Optional but Recommended) (Done)

To include user roles in JWT tokens:

1. Go to **Auth Pipeline** > **Rules** (or **Actions** > **Flows** in newer Auth0 versions)
2. Create a new rule/action to add custom claims:

```javascript
function addCustomClaims(user, context, callback) {
  const namespace = 'https://yourapp.com/';
  context.idToken[namespace + 'roles'] = user.app_metadata.roles || ['USER'];
  context.accessToken[namespace + 'roles'] = user.app_metadata.roles || ['USER'];
  callback(null, user, context);
}
```

### 4. Environment Variables (Done)

Set the following environment variables or update `application.properties`:

```properties
# Replace with your actual Auth0 domain and API identifier
AUTH0_DOMAIN=your-domain.auth0.com
AUTH0_AUDIENCE=https://your-api-identifier
```

### 5. Update Application Properties (Done)

The application is already configured to use these environment variables:

```properties
# Auth0 Configuration
auth0.audience=${AUTH0_AUDIENCE:your-api-identifier}
auth0.domain=${AUTH0_DOMAIN:your-domain.auth0.com}
spring.security.oauth2.resourceserver.jwt.issuer-uri=https://${auth0.domain}/
spring.security.oauth2.resourceserver.jwt.audiences=${auth0.audience}
```

## Testing the Integration

### 1. Get an Access Token

You can get an access token from Auth0 in several ways:

#### Option A: Using Auth0 Test Tab 
1. Go to your API in Auth0 Dashboard
2. Click on the **Test** tab
3. Copy the access token

#### Option B: Using cURL
```bash
curl --request POST \
  --url https://YOUR_DOMAIN.auth0.com/oauth/token \
  --header 'content-type: application/json' \
  --data '{
    "client_id": "YOUR_CLIENT_ID",
    "client_secret": "YOUR_CLIENT_SECRET",
    "audience": "https://your-api-identifier",
    "grant_type": "client_credentials"
  }'
```

### 2. Test API Endpoints

Use the access token to test your protected endpoints:

```bash
# Test user profile endpoint
curl -H "Authorization: Bearer YOUR_ACCESS_TOKEN" \
  http://localhost:8080/api/auth/profile

# Test verify endpoint
curl -H "Authorization: Bearer YOUR_ACCESS_TOKEN" \
  http://localhost:8080/api/auth/verify

# Test protected endpoints
curl -H "Authorization: Bearer YOUR_ACCESS_TOKEN" \
  http://localhost:8080/users
```

## Frontend Integration

Your frontend application should:

1. Authenticate users with Auth0
2. Obtain access tokens
3. Include the access token in API requests:

```javascript
// Example using fetch
fetch('/api/users', {
  headers: {
    'Authorization': `Bearer ${accessToken}`,
    'Content-Type': 'application/json'
  }
})
```

## Key Changes Made

1. **Removed custom JWT generation**: Auth0 now handles token generation
2. **Added Auth0 JWT validation**: Tokens are validated against Auth0's public keys
3. **Updated security configuration**: Uses OAuth2 Resource Server with JWT
4. **New endpoints**: `/api/auth/profile` and `/api/auth/verify` for user information
5. **Role extraction**: Supports multiple ways to extract roles from Auth0 tokens

## Troubleshooting

1. **Invalid audience**: Ensure the `AUTH0_AUDIENCE` matches your API identifier
2. **Invalid issuer**: Ensure the `AUTH0_DOMAIN` is correct
3. **Token validation fails**: Check that your Auth0 API is configured with RS256
4. **No roles found**: Verify your custom claims rule is working and user has roles assigned

## Security Notes

- Access tokens are validated against Auth0's public keys
- Tokens are verified for expiration, audience, and issuer
- The application no longer stores user credentials locally
- All authentication is handled by Auth0
