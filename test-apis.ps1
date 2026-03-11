# FaithLink API Testing Script
# Run this PowerShell script to test your APIs

# Base URL
$baseUrl = "http://localhost:8080/api"

Write-Host "🚀 Testing FaithLink Core API" -ForegroundColor Green
Write-Host "================================" -ForegroundColor Green

# Test 1: Create a Role
Write-Host "`n📝 Test 1: Creating a Role..." -ForegroundColor Yellow
try {
    $roleBody = @{
        name = "USER"
        description = "Regular user role"
    } | ConvertTo-Json
    
    $roleResponse = Invoke-RestMethod -Uri "$baseUrl/roles" -Method POST -Body $roleBody -ContentType "application/json"
    Write-Host "✅ Role created successfully:" -ForegroundColor Green
    Write-Host $roleResponse | ConvertTo-Json -Depth 3
} catch {
    Write-Host "❌ Failed to create role: $($_.Exception.Message)" -ForegroundColor Red
}

# Test 2: Create a User
Write-Host "`n👤 Test 2: Creating a User..." -ForegroundColor Yellow
try {
    $userBody = @{
        firstName = "John"
        lastName = "Doe"
        email = "john.doe@example.com"
        password = "password123"
        phone = "+1234567890"
    } | ConvertTo-Json
    
    $userResponse = Invoke-RestMethod -Uri "$baseUrl/users" -Method POST -Body $userBody -ContentType "application/json"
    Write-Host "✅ User created successfully:" -ForegroundColor Green
    Write-Host $userResponse | ConvertTo-Json -Depth 3
} catch {
    Write-Host "❌ Failed to create user: $($_.Exception.Message)" -ForegroundColor Red
}

# Test 3: Create a Church
Write-Host "`n⛪ Test 3: Creating a Church..." -ForegroundColor Yellow
try {
    $churchBody = @{
        name = "Faith Community Church"
        address = "123 Main Street, City, State 12345"
        phone = "+1234567890"
        email = "info@faithchurch.com"
        website = "https://faithchurch.com"
        description = "A welcoming community of believers"
    } | ConvertTo-Json
    
    $churchResponse = Invoke-RestMethod -Uri "$baseUrl/churches" -Method POST -Body $churchBody -ContentType "application/json"
    Write-Host "✅ Church created successfully:" -ForegroundColor Green
    Write-Host $churchResponse | ConvertTo-Json -Depth 3
} catch {
    Write-Host "❌ Failed to create church: $($_.Exception.Message)" -ForegroundColor Red
}

# Test 4: Get All Data
Write-Host "`n📊 Test 4: Retrieving All Data..." -ForegroundColor Yellow

try {
    Write-Host "`n📋 All Roles:" -ForegroundColor Cyan
    $roles = Invoke-RestMethod -Uri "$baseUrl/roles" -Method GET
    $roles | ConvertTo-Json -Depth 3
} catch {
    Write-Host "❌ Failed to get roles: $($_.Exception.Message)" -ForegroundColor Red
}

try {
    Write-Host "`n👥 All Users:" -ForegroundColor Cyan
    $users = Invoke-RestMethod -Uri "$baseUrl/users" -Method GET
    $users | ConvertTo-Json -Depth 3
} catch {
    Write-Host "❌ Failed to get users: $($_.Exception.Message)" -ForegroundColor Red
}

try {
    Write-Host "`n⛪ All Churches:" -ForegroundColor Cyan
    $churches = Invoke-RestMethod -Uri "$baseUrl/churches" -Method GET
    $churches | ConvertTo-Json -Depth 3
} catch {
    Write-Host "❌ Failed to get churches: $($_.Exception.Message)" -ForegroundColor Red
}

Write-Host "`n🎉 API Testing Complete!" -ForegroundColor Green
Write-Host "Check the H2 console at http://localhost:8080/h2-console to see the data" -ForegroundColor Yellow
