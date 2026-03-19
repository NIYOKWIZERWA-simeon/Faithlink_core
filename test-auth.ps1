# FaithLink Core API - Authentication Testing Script
# Run this PowerShell script to test authentication endpoints

# Base URL
$baseUrl = "http://localhost:8080/api"

Write-Host "🔐 Testing FaithLink Core API Authentication" -ForegroundColor Green
Write-Host "=============================================" -ForegroundColor Green

# Test 1: Register a new user
Write-Host "`n📝 Test 1: User Registration..." -ForegroundColor Yellow
try {
    $registerBody = @{
        firstName = "John"
        lastName = "Doe"
        email = "john.doe@example.com"
        password = "password123"
        phone = "+1234567890"
    } | ConvertTo-Json
    
    $registerResponse = Invoke-RestMethod -Uri "$baseUrl/auth/register" -Method POST -Body $registerBody -ContentType "application/json"
    Write-Host "✅ User registered successfully!" -ForegroundColor Green
    Write-Host "User ID: $($registerResponse.user.id)" -ForegroundColor Cyan
    Write-Host "Email: $($registerResponse.user.email)" -ForegroundColor Cyan
    $token = $registerResponse.token
    Write-Host "Token: $($token.Substring(0, 50))..." -ForegroundColor Cyan
} catch {
    Write-Host "❌ Registration failed: $($_.Exception.Message)" -ForegroundColor Red
    if ($_.Exception.Response -and $_.Exception.Response.StatusCode -eq 409) {
        Write-Host "User already exists, trying login..." -ForegroundColor Yellow
    }
}

# Test 2: Login
Write-Host "`n🔑 Test 2: User Login..." -ForegroundColor Yellow
try {
    $loginBody = @{
        email = "john.doe@example.com"
        password = "password123"
    } | ConvertTo-Json
    
    $loginResponse = Invoke-RestMethod -Uri "$baseUrl/auth/login" -Method POST -Body $loginBody -ContentType "application/json"
    Write-Host "✅ Login successful!" -ForegroundColor Green
    Write-Host "User: $($loginResponse.user.firstName) $($loginResponse.user.lastName)" -ForegroundColor Cyan
    $token = $loginResponse.token
    Write-Host "Token: $($token.Substring(0, 50))..." -ForegroundColor Cyan
} catch {
    Write-Host "❌ Login failed: $($_.Exception.Message)" -ForegroundColor Red
    return
}

# Test 3: Get User Profile
Write-Host "`n👤 Test 3: Get User Profile..." -ForegroundColor Yellow
try {
    $headers = @{
        "Authorization" = "Bearer $token"
    }
    
    $profileResponse = Invoke-RestMethod -Uri "$baseUrl/auth/profile" -Method GET -Headers $headers
    Write-Host "✅ Profile retrieved successfully!" -ForegroundColor Green
    Write-Host "Name: $($profileResponse.firstName) $($profileResponse.lastName)" -ForegroundColor Cyan
    Write-Host "Email: $($profileResponse.email)" -ForegroundColor Cyan
    Write-Host "Active: $($profileResponse.isActive)" -ForegroundColor Cyan
} catch {
    Write-Host "❌ Profile retrieval failed: $($_.Exception.Message)" -ForegroundColor Red
}

# Test 4: Token Validation
Write-Host "`n✅ Test 4: Token Validation..." -ForegroundColor Yellow
try {
    $headers = @{
        "Authorization" = "Bearer $token"
    }
    
    $validateResponse = Invoke-RestMethod -Uri "$baseUrl/auth/validate" -Method GET -Headers $headers
    Write-Host "✅ Token is valid!" -ForegroundColor Green
    Write-Host "Username: $($validateResponse.username)" -ForegroundColor Cyan
    Write-Host "Roles: $($validateResponse.roles -join ', ')" -ForegroundColor Cyan
} catch {
    Write-Host "❌ Token validation failed: $($_.Exception.Message)" -ForegroundColor Red
}

# Test 5: Access Protected Endpoint (Users)
Write-Host "`n🔒 Test 5: Access Protected Endpoint (Users)..." -ForegroundColor Yellow
try {
    $headers = @{
        "Authorization" = "Bearer $token"
    }
    
    $usersResponse = Invoke-RestMethod -Uri "$baseUrl/users/email/john.doe@example.com" -Method GET -Headers $headers
    Write-Host "✅ Protected endpoint access successful!" -ForegroundColor Green
    Write-Host "User ID: $($usersResponse.id)" -ForegroundColor Cyan
    Write-Host "Email: $($usersResponse.email)" -ForegroundColor Cyan
} catch {
    Write-Host "❌ Protected endpoint access failed: $($_.Exception.Message)" -ForegroundColor Red
}

# Test 6: Access Public Endpoint (Churches)
Write-Host "`n🌐 Test 6: Access Public Endpoint (Churches)..." -ForegroundColor Yellow
try {
    $churchesResponse = Invoke-RestMethod -Uri "$baseUrl/churches" -Method GET
    Write-Host "✅ Public endpoint access successful!" -ForegroundColor Green
    Write-Host "Churches count: $($churchesResponse.totalElements)" -ForegroundColor Cyan
} catch {
    Write-Host "❌ Public endpoint access failed: $($_.Exception.Message)" -ForegroundColor Red
}

# Test 7: Token Refresh
Write-Host "`n🔄 Test 7: Token Refresh..." -ForegroundColor Yellow
try {
    $refreshBody = @{
        token = $token
    } | ConvertTo-Json
    
    $refreshResponse = Invoke-RestMethod -Uri "$baseUrl/auth/refresh" -Method POST -Body $refreshBody -ContentType "application/json"
    Write-Host "✅ Token refreshed successfully!" -ForegroundColor Green
    Write-Host "New Token: $($refreshResponse.token.Substring(0, 50))..." -ForegroundColor Cyan
    $newToken = $refreshResponse.token
} catch {
    Write-Host "❌ Token refresh failed: $($_.Exception.Message)" -ForegroundColor Red
}

# Test 8: Change Password
Write-Host "`n🔐 Test 8: Change Password..." -ForegroundColor Yellow
try {
    $headers = @{
        "Authorization" = "Bearer $token"
    }
    
    $passwordBody = @{
        currentPassword = "password123"
        newPassword = "newpassword456"
        confirmPassword = "newpassword456"
    } | ConvertTo-Json
    
    Invoke-RestMethod -Uri "$baseUrl/auth/password" -Method PUT -Headers $headers -Body $passwordBody -ContentType "application/json"
    Write-Host "✅ Password changed successfully!" -ForegroundColor Green
} catch {
    Write-Host "❌ Password change failed: $($_.Exception.Message)" -ForegroundColor Red
}

# Test 9: Login with New Password
Write-Host "`n🔑 Test 9: Login with New Password..." -ForegroundColor Yellow
try {
    $loginBody = @{
        email = "john.doe@example.com"
        password = "newpassword456"
    } | ConvertTo-Json
    
    $loginResponse = Invoke-RestMethod -Uri "$baseUrl/auth/login" -Method POST -Body $loginBody -ContentType "application/json"
    Write-Host "✅ Login with new password successful!" -ForegroundColor Green
    $token = $loginResponse.token
} catch {
    Write-Host "❌ Login with new password failed: $($_.Exception.Message)" -ForegroundColor Red
}

# Test 10: Logout
Write-Host "`n👋 Test 10: Logout..." -ForegroundColor Yellow
try {
    $headers = @{
        "Authorization" = "Bearer $token"
    }
    
    $logoutResponse = Invoke-RestMethod -Uri "$baseUrl/auth/logout" -Method POST -Headers $headers
    Write-Host "✅ Logout successful!" -ForegroundColor Green
    Write-Host "Message: $($logoutResponse.message)" -ForegroundColor Cyan
} catch {
    Write-Host "❌ Logout failed: $($_.Exception.Message)" -ForegroundColor Red
}

Write-Host "`n🎉 Authentication Testing Complete!" -ForegroundColor Green
Write-Host "=================================" -ForegroundColor Green
Write-Host "All authentication features have been tested." -ForegroundColor Yellow
Write-Host "Check the H2 console at http://localhost:8080/h2-console to see the data" -ForegroundColor Cyan
