# Simple API Test Script
$baseUrl = "http://localhost:8080/api"

Write-Host "Testing FaithLink API..." -ForegroundColor Green

# Test 1: Create Role
Write-Host "Creating Role..." -ForegroundColor Yellow
$roleBody = '{"name":"USER","description":"Regular user role"}'
try {
    $roleResponse = Invoke-RestMethod -Uri "$baseUrl/roles" -Method POST -Body $roleBody -ContentType "application/json"
    Write-Host "Role created: " $roleResponse.name -ForegroundColor Green
} catch {
    Write-Host "Error creating role" -ForegroundColor Red
}

# Test 2: Create User
Write-Host "Creating User..." -ForegroundColor Yellow
$userBody = '{"firstName":"John","lastName":"Doe","email":"john@example.com","password":"password123"}'
try {
    $userResponse = Invoke-RestMethod -Uri "$baseUrl/users" -Method POST -Body $userBody -ContentType "application/json"
    Write-Host "User created: " $userResponse.firstName $userResponse.lastName -ForegroundColor Green
} catch {
    Write-Host "Error creating user" -ForegroundColor Red
}

# Test 3: Create Church
Write-Host "Creating Church..." -ForegroundColor Yellow
$churchBody = '{"name":"Faith Church","address":"123 Main St","email":"info@faithchurch.com"}'
try {
    $churchResponse = Invoke-RestMethod -Uri "$baseUrl/churches" -Method POST -Body $churchBody -ContentType "application/json"
    Write-Host "Church created: " $churchResponse.name -ForegroundColor Green
} catch {
    Write-Host "Error creating church" -ForegroundColor Red
}

# Test 4: Get All Data
Write-Host "Getting all data..." -ForegroundColor Yellow
try {
    $roles = Invoke-RestMethod -Uri "$baseUrl/roles" -Method GET
    $users = Invoke-RestMethod -Uri "$baseUrl/users" -Method GET
    $churches = Invoke-RestMethod -Uri "$baseUrl/churches" -Method GET
    
    Write-Host "Roles count:" $roles.Count -ForegroundColor Cyan
    Write-Host "Users count:" $users.Count -ForegroundColor Cyan
    Write-Host "Churches count:" $churches.Count -ForegroundColor Cyan
} catch {
    Write-Host "Error getting data" -ForegroundColor Red
}

Write-Host "Test complete!" -ForegroundColor Green
