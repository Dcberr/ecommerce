#!/bin/bash

# =============================================================================
# E-commerce API Test Script
# This script tests the backend API endpoints for creating:
# - Categories
# - Products  
# - Product Listings
# - Feedbacks
# =============================================================================

# Configuration
BASE_URL="${BASE_URL:-http://localhost:8003}"
# AUTH_TOKEN="${AUTH_TOKEN:-}"  # Set this if authentication is required

# Database Configuration (for direct user creation)
DB_HOST="${DB_HOST:-localhost}"
DB_PORT="${DB_PORT:-5432}"
DB_NAME="${DB_NAME:-ecommerce}"
DB_USER="${DB_USER:-admin}"
DB_PASSWORD="${DB_PASSWORD:-admin}"

# Generated User IDs (will be set after user creation)
SELLER_ID_1=""
SELLER_ID_2=""
BUYER_ID_1=""
BUYER_ID_2=""

# Colors for output
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
NC='\033[0m' # No Color


# Helper function to print colored output
print_header() {
    echo -e "\n${BLUE}=============================================${NC}"
    echo -e "${BLUE}$1${NC}"
    echo -e "${BLUE}=============================================${NC}"
}

print_success() {
    echo -e "${GREEN}✓ $1${NC}"
}

print_error() {
    echo -e "${RED}✗ $1${NC}"
}

print_info() {
    echo -e "${YELLOW}→ $1${NC}"
}

# Helper function for curl requests
# Uses temporary file for data to avoid "Argument list too long" error
do_curl() {
    local method=$1
    local endpoint=$2
    local data=$3
    local tmp_file=""
    local result=""
    
    if [ -n "$data" ]; then
        tmp_file=$(mktemp)
        printf '%s' "$data" > "$tmp_file"
    fi
    
    if [ -n "$AUTH_TOKEN" ]; then
        if [ -n "$data" ]; then
            result=$(curl -s -X "$method" "$BASE_URL$endpoint" \
                -H "Content-Type: application/json" \
                -H "Authorization: Bearer $AUTH_TOKEN" \
                -d "@$tmp_file")
        else
            result=$(curl -s -X "$method" "$BASE_URL$endpoint" \
                -H "Content-Type: application/json" \
                -H "Authorization: Bearer $AUTH_TOKEN")
        fi
    else
        if [ -n "$data" ]; then
            result=$(curl -s -X "$method" "$BASE_URL$endpoint" \
                -H "Content-Type: application/json" \
                -d "@$tmp_file")
        else
            result=$(curl -s -X "$method" "$BASE_URL$endpoint" \
                -H "Content-Type: application/json")
        fi
    fi
    
    # Clean up temp file
    if [ -n "$tmp_file" ] && [ -f "$tmp_file" ]; then
        rm -f "$tmp_file"
    fi
    
    printf '%s' "$result"
}

# =============================================================================
# Test: Create Users (Buyers and Sellers) via Database
# =============================================================================
create_users() {
    print_header "Creating Users (Buyers and Sellers)"
    
    # Check if psql is available
    if ! command -v psql &> /dev/null; then
        print_error "psql is not installed. Please install PostgreSQL client."
        print_info "On Ubuntu/Debian: sudo apt-get install postgresql-client"
        print_info "On macOS: brew install postgresql"
        return 1
    fi
    
    export PGPASSWORD="$DB_PASSWORD"
    
    # Test database connection first
    print_info "Testing database connection to $DB_HOST:$DB_PORT/$DB_NAME..."
    if ! psql -h "$DB_HOST" -p "$DB_PORT" -U "$DB_USER" -d "$DB_NAME" -c "SELECT 1;" > /dev/null 2>&1; then
        print_error "Failed to connect to database. Check your DB_HOST, DB_PORT, DB_USER, DB_PASSWORD, DB_NAME settings."
        unset PGPASSWORD
        return 1
    fi
    print_success "Database connection successful"
    
    # Helper function to create or get user
    create_or_get_user() {
        local email=$1
        local name=$2
        local picture=$3
        local tel=$4
        local address=$5
        local province=$6
        local district=$7
        local ward=$8
        local role=$9
        local wallet_amount=${10}
        
        # Check if user already exists
        local existing_id=$(psql -h "$DB_HOST" -p "$DB_PORT" -U "$DB_USER" -d "$DB_NAME" -tAc \
            "SELECT id FROM users WHERE email = '$email';")
        
        if [ -n "$existing_id" ]; then
            echo "$existing_id"
            return
        fi
        
        # Insert new user
        local new_id=$(psql -h "$DB_HOST" -p "$DB_PORT" -U "$DB_USER" -d "$DB_NAME" -tAc \
            "INSERT INTO users (id, email, name, picture, tel, address, province, district, ward, user_role)
             VALUES (gen_random_uuid(), '$email', '$name', '$picture', '$tel', '$address', '$province', '$district', '$ward', '$role')
             RETURNING id;")
        
        if [ -n "$new_id" ]; then
            # Create wallet for user
            psql -h "$DB_HOST" -p "$DB_PORT" -U "$DB_USER" -d "$DB_NAME" -c \
                "INSERT INTO wallet (id, amount, user_id) VALUES (gen_random_uuid(), $wallet_amount, '$new_id')
                 ON CONFLICT (user_id) DO NOTHING;" > /dev/null 2>&1
            echo "$new_id"
        fi
    }
    
    print_info "Creating Seller 1: John Farm"
    SELLER_ID_1=$(create_or_get_user \
        "seller1@example.com" \
        "John Farm" \
        "https://example.com/avatar/seller1.jpg" \
        "0901234567" \
        "123 Farm Road" \
        "Ho Chi Minh" \
        "District 9" \
        "Long Truong" \
        "SELLER" \
        "1000000")
    if [ -n "$SELLER_ID_1" ]; then
        print_success "Seller 1 ID: $SELLER_ID_1"
    else
        print_error "Failed to create Seller 1"
    fi
    
    print_info "Creating Seller 2: Fresh Market"
    SELLER_ID_2=$(create_or_get_user \
        "seller2@example.com" \
        "Fresh Market" \
        "https://example.com/avatar/seller2.jpg" \
        "0902345678" \
        "456 Market Street" \
        "Ha Noi" \
        "Cau Giay" \
        "Dich Vong" \
        "SELLER" \
        "500000")
    if [ -n "$SELLER_ID_2" ]; then
        print_success "Seller 2 ID: $SELLER_ID_2"
    else
        print_error "Failed to create Seller 2"
    fi
    
    print_info "Creating Buyer 1: Alice Consumer"
    BUYER_ID_1=$(create_or_get_user \
        "buyer1@example.com" \
        "Alice Consumer" \
        "https://example.com/avatar/buyer1.jpg" \
        "0903456789" \
        "789 Buyer Lane" \
        "Ho Chi Minh" \
        "District 1" \
        "Ben Nghe" \
        "BUYER" \
        "2000000")
    if [ -n "$BUYER_ID_1" ]; then
        print_success "Buyer 1 ID: $BUYER_ID_1"
    else
        print_error "Failed to create Buyer 1"
    fi
    
    print_info "Creating Buyer 2: Bob Shopper"
    BUYER_ID_2=$(create_or_get_user \
        "buyer2@example.com" \
        "Bob Shopper" \
        "https://example.com/avatar/buyer2.jpg" \
        "0904567890" \
        "321 Shopping Ave" \
        "Da Nang" \
        "Hai Chau" \
        "Thach Thang" \
        "BUYER" \
        "1500000")
    if [ -n "$BUYER_ID_2" ]; then
        print_success "Buyer 2 ID: $BUYER_ID_2"
    else
        print_error "Failed to create Buyer 2"
    fi
    
    unset PGPASSWORD
    
    print_success "All users created successfully!"
    echo ""
    echo "User IDs for reference:"
    echo "  SELLER_ID_1=$SELLER_ID_1"
    echo "  SELLER_ID_2=$SELLER_ID_2"
    echo "  BUYER_ID_1=$BUYER_ID_1"
    echo "  BUYER_ID_2=$BUYER_ID_2"
    
    # Export for use in other functions
    export SELLER_ID_1 SELLER_ID_2 BUYER_ID_1 BUYER_ID_2
}

# =============================================================================
# Test: Get Users from Database
# =============================================================================
get_users() {
    print_header "Getting Users from Database"
    
    if ! command -v psql &> /dev/null; then
        print_error "psql is not installed."
        return 1
    fi
    
    export PGPASSWORD="$DB_PASSWORD"
    
    print_info "Listing all users:"
    psql -h "$DB_HOST" -p "$DB_PORT" -U "$DB_USER" -d "$DB_NAME" -c "
        SELECT id, email, name, user_role, province FROM users ORDER BY user_role, name;
    " 2>/dev/null
    
    unset PGPASSWORD
    print_success "Users retrieved"
}

# =============================================================================
# Test: Create Categories
# =============================================================================
create_categories() {
    print_header "Creating Categories"
    
    # Read image base64 from file
    local SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
    local IMAGE_B64=""
    if [ -f "$SCRIPT_DIR/mango.png.b64.txt" ]; then
        IMAGE_B64=$(cat "$SCRIPT_DIR/mango.png.b64.txt" | tr -d '\n\r')
        print_info "Loaded image from mango.png.b64.txt (${#IMAGE_B64} chars)"
    else
        print_info "mango.png.b64.txt not found, creating categories without image blob"
    fi
    
    # Category 1: Vegetables
    print_info "Creating category: Vegetables"
    response=$(do_curl POST "/api/categories" "{\"name\":\"Vegetables\",\"description\":\"Fresh organic vegetables from local farms\",\"imageBlobString\":\"$IMAGE_B64\",\"imageType\":\"image/png\"}")
    echo "$response" | jq . 2>/dev/null || echo "$response"
    
    # Category 2: Fruits
    print_info "Creating category: Fruits"
    response=$(do_curl POST "/api/categories" "{\"name\":\"Fruits\",\"description\":\"Seasonal fruits from trusted suppliers\",\"imageBlobString\":\"$IMAGE_B64\",\"imageType\":\"image/png\"}")
    echo "$response" | jq . 2>/dev/null || echo "$response"
    
    # Category 3: Dairy
    print_info "Creating category: Dairy"
    response=$(do_curl POST "/api/categories" "{\"name\":\"Dairy\",\"description\":\"Milk, cheese, and other dairy products\",\"imageBlobString\":\"$IMAGE_B64\",\"imageType\":\"image/png\"}")
    echo "$response" | jq . 2>/dev/null || echo "$response"
    
    # Category 4: Meat
    print_info "Creating category: Meat"
    response=$(do_curl POST "/api/categories" "{\"name\":\"Meat\",\"description\":\"Fresh meat and poultry products\",\"imageBlobString\":\"$IMAGE_B64\",\"imageType\":\"image/png\"}")
    echo "$response" | jq . 2>/dev/null || echo "$response"
    
    # Category 5: Seafood
    print_info "Creating category: Seafood"
    response=$(do_curl POST "/api/categories" "{\"name\":\"Seafood\",\"description\":\"Fresh seafood from coastal regions\",\"imageBlobString\":\"$IMAGE_B64\",\"imageType\":\"image/png\"}")
    echo "$response" | jq . 2>/dev/null || echo "$response"
    
    print_success "Categories created"
}

# =============================================================================
# Test: Get All Categories
# =============================================================================
get_categories() {
    print_header "Getting All Categories"
    
    response=$(do_curl GET "/api/categories" "")
    echo "$response" | jq . 2>/dev/null || echo "$response"
    
    print_success "Categories retrieved"
}

# =============================================================================
# Test: Create Products
# ProductUnitType values: KILOGRAM, GRAM, PIECE, PACK, LITER, BOX (check your enum)
# =============================================================================
create_products() {
    print_header "Creating Products"
    
    # Read image base64 from file
    local SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
    local IMAGE_B64=""
    if [ -f "$SCRIPT_DIR/mango.png.b64.txt" ]; then
        IMAGE_B64=$(cat "$SCRIPT_DIR/mango.png.b64.txt" | tr -d '\n\r')
        print_info "Loaded image from mango.png.b64.txt (${#IMAGE_B64} chars)"
    else
        print_info "mango.png.b64.txt not found, creating products without image blob"
    fi
    
    # Product 1: Tomato
    print_info "Creating product: Tomato"
    response=$(do_curl POST "/api/products" "{\"name\":\"Tomato\",\"baseUnit\":\"KILOGRAM\",\"categoryIds\":[1],\"imageBlobString\":\"$IMAGE_B64\",\"imageType\":\"image/png\"}")
    echo "$response" | jq . 2>/dev/null || echo "$response"
    
    # Product 2: Apple
    print_info "Creating product: Apple"
    response=$(do_curl POST "/api/products" "{\"name\":\"Apple\",\"baseUnit\":\"KILOGRAM\",\"categoryIds\":[2],\"imageBlobString\":\"$IMAGE_B64\",\"imageType\":\"image/png\"}")
    echo "$response" | jq . 2>/dev/null || echo "$response"
    
    # Product 3: Milk
    print_info "Creating product: Milk"
    response=$(do_curl POST "/api/products" "{\"name\":\"Fresh Milk\",\"baseUnit\":\"LITER\",\"categoryIds\":[3],\"imageBlobString\":\"$IMAGE_B64\",\"imageType\":\"image/png\"}")
    echo "$response" | jq . 2>/dev/null || echo "$response"
    
    # Product 4: Chicken Breast
    print_info "Creating product: Chicken Breast"
    response=$(do_curl POST "/api/products" "{\"name\":\"Chicken Breast\",\"baseUnit\":\"KILOGRAM\",\"categoryIds\":[4],\"imageBlobString\":\"$IMAGE_B64\",\"imageType\":\"image/png\"}")
    echo "$response" | jq . 2>/dev/null || echo "$response"
    
    # Product 5: Salmon
    print_info "Creating product: Salmon"
    response=$(do_curl POST "/api/products" "{\"name\":\"Salmon Fillet\",\"baseUnit\":\"KILOGRAM\",\"categoryIds\":[5],\"imageBlobString\":\"$IMAGE_B64\",\"imageType\":\"image/png\"}")
    echo "$response" | jq . 2>/dev/null || echo "$response"
    
    # Product 6: Carrot
    print_info "Creating product: Carrot"
    response=$(do_curl POST "/api/products" "{\"name\":\"Carrot\",\"baseUnit\":\"KILOGRAM\",\"categoryIds\":[1],\"imageBlobString\":\"$IMAGE_B64\",\"imageType\":\"image/png\"}")
    echo "$response" | jq . 2>/dev/null || echo "$response"
    
    print_success "Products created"
}

# =============================================================================
# Test: Get All Products
# =============================================================================
get_products() {
    print_header "Getting All Products"
    
    response=$(do_curl GET "/api/products?page=0&pageSize=10&sortBy=id&desc=false" "")
    echo "$response" | jq . 2>/dev/null || echo "$response"
    
    print_success "Products retrieved"
}

# =============================================================================
# Test: Create Product Listings
# Note: Requires valid seller IDs from your database
# =============================================================================
create_product_listings() {
    print_header "Creating Product Listings"
    
    # Use exported IDs or fall back to environment variables or defaults
    local SELLER_ID_1="${SELLER_ID_1:-$(get_seller_id 1)}"
    local SELLER_ID_2="${SELLER_ID_2:-$(get_seller_id 2)}"
    
    if [ -z "$SELLER_ID_1" ] || [ "$SELLER_ID_1" = "seller-uuid-1" ]; then
        print_error "SELLER_ID_1 not set. Run create_users first or set SELLER_ID_1 environment variable."
        return 1
    fi
    
    # Read image base64 from file
    local SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
    local IMAGE_B64=""
    if [ -f "$SCRIPT_DIR/mango.png.b64.txt" ]; then
        IMAGE_B64=$(cat "$SCRIPT_DIR/mango.png.b64.txt" | tr -d '\n\r')
        print_info "Loaded image from mango.png.b64.txt (${#IMAGE_B64} chars)"
    else
        print_error "mango.png.b64.txt not found, using empty image"
    fi
    
    # Product Listing 1: Tomato by Seller 1
    print_info "Creating product listing: Tomato by Seller 1"
    response=$(do_curl POST "/api/productListings" "{\"sellerId\":\"$SELLER_ID_1\",\"productId\":1,\"stock\":100,\"price\":25000,\"name\":\"Fresh Organic Tomatoes\",\"description\":\"Locally grown organic tomatoes, perfect for salads and cooking\",\"imageBlobString\":\"$IMAGE_B64\",\"imageType\":\"image/png\"}")
    echo "$response" | jq . 2>/dev/null || echo "$response"
    
    # Product Listing 2: Apple by Seller 1
    print_info "Creating product listing: Apple by Seller 1"
    response=$(do_curl POST "/api/productListings" "{\"sellerId\":\"$SELLER_ID_1\",\"productId\":2,\"stock\":200,\"price\":45000,\"name\":\"Premium Red Apples\",\"description\":\"Sweet and crispy red apples imported from Da Lat\",\"imageBlobString\":\"$IMAGE_B64\",\"imageType\":\"image/png\"}")
    echo "$response" | jq . 2>/dev/null || echo "$response"
    
    # Product Listing 3: Milk by Seller 2
    print_info "Creating product listing: Milk by Seller 2"
    response=$(do_curl POST "/api/productListings" "{\"sellerId\":\"$SELLER_ID_2\",\"productId\":3,\"stock\":50,\"price\":35000,\"name\":\"Fresh Farm Milk\",\"description\":\"100% pure fresh milk from grass-fed cows\",\"imageBlobString\":\"$IMAGE_B64\",\"imageType\":\"image/png\"}")
    echo "$response" | jq . 2>/dev/null || echo "$response"
    
    # Product Listing 4: Chicken by Seller 2
    print_info "Creating product listing: Chicken by Seller 2"
    response=$(do_curl POST "/api/productListings" "{\"sellerId\":\"$SELLER_ID_2\",\"productId\":4,\"stock\":75,\"price\":120000,\"name\":\"Premium Chicken Breast\",\"description\":\"Fresh boneless chicken breast, hormone-free\",\"imageBlobString\":\"$IMAGE_B64\",\"imageType\":\"image/png\"}")
    echo "$response" | jq . 2>/dev/null || echo "$response"
    
    # Product Listing 5: Salmon by Seller 1
    print_info "Creating product listing: Salmon by Seller 1"
    response=$(do_curl POST "/api/productListings" "{\"sellerId\":\"$SELLER_ID_1\",\"productId\":5,\"stock\":30,\"price\":350000,\"name\":\"Norwegian Salmon Fillet\",\"description\":\"Premium grade salmon fillet, rich in omega-3\",\"imageBlobString\":\"$IMAGE_B64\",\"imageType\":\"image/png\"}")
    echo "$response" | jq . 2>/dev/null || echo "$response"
    
    print_success "Product listings created"
}

# =============================================================================
# Test: Get All Product Listings
# =============================================================================
get_product_listings() {
    print_header "Getting All Product Listings"
    
    response=$(do_curl GET "/api/productListings?page=0&pageSize=10&sortBy=price&desc=false" "")
    echo "$response" | jq . 2>/dev/null || echo "$response"
    
    print_success "Product listings retrieved"
}

# =============================================================================
# Test: Create Feedbacks
# Note: Requires valid buyer and seller IDs from your database
# =============================================================================
create_feedbacks() {
    print_header "Creating Feedbacks"
    
    # Use exported IDs or fall back to helper functions
    local BUYER_ID_1="${BUYER_ID_1:-$(get_buyer_id 1)}"
    local BUYER_ID_2="${BUYER_ID_2:-$(get_buyer_id 2)}"
    local SELLER_ID_1="${SELLER_ID_1:-$(get_seller_id 1)}"
    local SELLER_ID_2="${SELLER_ID_2:-$(get_seller_id 2)}"
    
    if [ -z "$BUYER_ID_1" ] || [ "$BUYER_ID_1" = "buyer-uuid-1" ]; then
        print_error "User IDs not set. Run create_users first."
        return 1
    fi
    
    # Feedback 1: Buyer 1 on Seller 1's Tomato (Product 1)
    print_info "Creating feedback: Buyer 1 on Tomato"
    response=$(do_curl POST "/api/feedbacks/$SELLER_ID_1/1/$BUYER_ID_1" "{\"comment\":\"Excellent quality tomatoes! Very fresh and tasty.\",\"rating\":5}")
    echo "$response" | jq . 2>/dev/null || echo "$response"
    
    # Feedback 2: Buyer 2 on Seller 1's Tomato (Product 1)
    print_info "Creating feedback: Buyer 2 on Tomato"
    response=$(do_curl POST "/api/feedbacks/$SELLER_ID_1/1/$BUYER_ID_2" "{\"comment\":\"Good product, fast delivery.\",\"rating\":4}")
    echo "$response" | jq . 2>/dev/null || echo "$response"
    
    # Feedback 3: Buyer 1 on Seller 1's Apple (Product 2)
    print_info "Creating feedback: Buyer 1 on Apple"
    response=$(do_curl POST "/api/feedbacks/$SELLER_ID_1/2/$BUYER_ID_1" "{\"comment\":\"Sweet and crispy apples, will buy again!\",\"rating\":5}")
    echo "$response" | jq . 2>/dev/null || echo "$response"
    
    # Feedback 4: Buyer 2 on Seller 2's Milk (Product 3)
    print_info "Creating feedback: Buyer 2 on Milk"
    response=$(do_curl POST "/api/feedbacks/$SELLER_ID_2/3/$BUYER_ID_2" "{\"comment\":\"Fresh milk, great taste!\",\"rating\":4}")
    echo "$response" | jq . 2>/dev/null || echo "$response"
    
    # Feedback 5: Buyer 1 on Seller 2's Chicken (Product 4)
    print_info "Creating feedback: Buyer 1 on Chicken"
    response=$(do_curl POST "/api/feedbacks/$SELLER_ID_2/4/$BUYER_ID_1" "{\"comment\":\"Very fresh chicken, nicely packed.\",\"rating\":5}")
    echo "$response" | jq . 2>/dev/null || echo "$response"
    
    print_success "Feedbacks created"
}

# =============================================================================
# Test: Get Feedbacks
# =============================================================================
get_feedbacks() {
    print_header "Getting Feedbacks for Product"
    
    local SELLER_ID_1="${SELLER_ID_1:-seller-uuid-1}"
    
    response=$(do_curl GET "/api/feedbacks/$SELLER_ID_1/1?page=0&pageSize=10&sortBy=rating&desc=true" "")
    echo "$response" | jq . 2>/dev/null || echo "$response"
    
    print_success "Feedbacks retrieved"
}

# =============================================================================
# Test: User Endpoints
# =============================================================================
get_my_info() {
    print_header "Getting My Info (requires auth)"
    
    if [ -z "$AUTH_TOKEN" ]; then
        print_error "AUTH_TOKEN is not set. Skipping this test."
        return
    fi
    
    response=$(do_curl GET "/api/user/my-info" "")
    echo "$response" | jq . 2>/dev/null || echo "$response"
    
    print_success "User info retrieved"
}

update_first_login_info() {
    print_header "Updating First Login Info (requires auth)"
    
    if [ -z "$AUTH_TOKEN" ]; then
        print_error "AUTH_TOKEN is not set. Skipping this test."
        return
    fi
    
    response=$(do_curl POST "/api/user/first-login" '{"tel":"0123456789","address":"123 Main Street","province":"Ho Chi Minh","district":"District 1","ward":"Ben Nghe"}')
    echo "$response" | jq . 2>/dev/null || echo "$response"
    
    print_success "First login info updated"
}

# =============================================================================
# Drop All Data
# =============================================================================
drop_all() {
    print_header "Dropping All Data"
    
    # Check if psql is available
    if ! command -v psql &> /dev/null; then
        print_error "psql is not installed. Please install PostgreSQL client."
        return 1
    fi
    
    export PGPASSWORD="$DB_PASSWORD"
    
    # Confirm before dropping
    echo -e "${RED}WARNING: This will delete ALL data from the database!${NC}"
    read -p "Are you sure you want to continue? (yes/no): " confirm
    if [ "$confirm" != "yes" ]; then
        print_info "Aborted."
        return 0
    fi
    
    print_info "Dropping feedbacks..."
    psql -h "$DB_HOST" -p "$DB_PORT" -U "$DB_USER" -d "$DB_NAME" -tAc "DELETE FROM feedback;" 2>/dev/null
    
    print_info "Dropping product listings..."
    psql -h "$DB_HOST" -p "$DB_PORT" -U "$DB_USER" -d "$DB_NAME" -tAc "DELETE FROM product_listings;" 2>/dev/null
    
    print_info "Dropping products..."
    psql -h "$DB_HOST" -p "$DB_PORT" -U "$DB_USER" -d "$DB_NAME" -tAc "DELETE FROM products;" 2>/dev/null
    
    print_info "Dropping categories..."
    psql -h "$DB_HOST" -p "$DB_PORT" -U "$DB_USER" -d "$DB_NAME" -tAc "DELETE FROM categories;" 2>/dev/null
    
    print_info "Dropping product categories..."
    psql -h "$DB_HOST" -p "$DB_PORT" -U "$DB_USER" -d "$DB_NAME" -tAc "DELETE FROM product_categories;" 2>/dev/null

    print_info "Dropping wallets..."
    psql -h "$DB_HOST" -p "$DB_PORT" -U "$DB_USER" -d "$DB_NAME" -tAc "DELETE FROM wallets;" 2>/dev/null
    
    print_info "Dropping users..."
    psql -h "$DB_HOST" -p "$DB_PORT" -U "$DB_USER" -d "$DB_NAME" -tAc "DELETE FROM users;" 2>/dev/null
    
    unset PGPASSWORD
    
    # Clear cached IDs
    SELLER_ID_1=""
    SELLER_ID_2=""
    BUYER_ID_1=""
    BUYER_ID_2=""
    
    print_success "All data dropped successfully"
}

drop_all_force() {
    # Non-interactive version for command line
    print_header "Dropping All Data (Force)"
    
    if ! command -v psql &> /dev/null; then
        print_error "psql is not installed. Please install PostgreSQL client."
        return 1
    fi
    
    export PGPASSWORD="$DB_PASSWORD"

    print_info "Dropping feedbacks..."
    psql -h "$DB_HOST" -p "$DB_PORT" -U "$DB_USER" -d "$DB_NAME" -tAc "DELETE FROM feedback;" 2>/dev/null
    
    print_info "Dropping product listings..."
    psql -h "$DB_HOST" -p "$DB_PORT" -U "$DB_USER" -d "$DB_NAME" -tAc "DELETE FROM product_listings;" 2>/dev/null
    
    print_info "Dropping products..."
    psql -h "$DB_HOST" -p "$DB_PORT" -U "$DB_USER" -d "$DB_NAME" -tAc "DELETE FROM products;" 2>/dev/null
    
    print_info "Dropping categories..."
    psql -h "$DB_HOST" -p "$DB_PORT" -U "$DB_USER" -d "$DB_NAME" -tAc "DELETE FROM categories;" 2>/dev/null
    
    print_info "Dropping product categories..."
    psql -h "$DB_HOST" -p "$DB_PORT" -U "$DB_USER" -d "$DB_NAME" -tAc "DELETE FROM product_categories;" 2>/dev/null

    print_info "Dropping wallets..."
    psql -h "$DB_HOST" -p "$DB_PORT" -U "$DB_USER" -d "$DB_NAME" -tAc "DELETE FROM wallets;" 2>/dev/null
    
    print_info "Dropping users..."
    psql -h "$DB_HOST" -p "$DB_PORT" -U "$DB_USER" -d "$DB_NAME" -tAc "DELETE FROM users;" 2>/dev/null
    
    unset PGPASSWORD
    
    SELLER_ID_1=""
    SELLER_ID_2=""
    BUYER_ID_1=""
    BUYER_ID_2=""
    
    print_success "All data dropped successfully"
}

# =============================================================================
# Main Menu
# =============================================================================
show_menu() {
    echo -e "\n${BLUE}E-commerce API Test Script${NC}"
    echo "================================"
    echo "1. Create Users (Buyers & Sellers)"
    echo "2. Get Users"
    echo "3. Create Categories"
    echo "4. Get All Categories"
    echo "5. Create Products"
    echo "6. Get All Products"
    echo "7. Create Product Listings"
    echo "8. Get All Product Listings"
    echo "9. Create Feedbacks"
    echo "10. Get Feedbacks"
    echo "11. Get My Info"
    echo "12. Update First Login Info"
    echo "13. Run All Tests (Create)"
    echo "14. Run All Tests (Get)"
    echo -e "${RED}15. Drop All Data${NC}"
    echo "0. Exit"
    echo ""
    echo "Current BASE_URL: $BASE_URL"
    echo "Database: $DB_HOST:$DB_PORT/$DB_NAME"
    echo "AUTH_TOKEN set: $([ -n "$AUTH_TOKEN" ] && echo 'Yes' || echo 'No')"
    echo "SELLER_ID_1: ${SELLER_ID_1:-not set}"
    echo "BUYER_ID_1: ${BUYER_ID_1:-not set}"
    echo ""
}

run_all_create_tests() {
    create_users
    sleep 1
    create_categories
    sleep 1
    create_products
    sleep 1
    create_product_listings
    sleep 1
    create_feedbacks
}

run_all_get_tests() {
    get_users
    get_categories
    get_products
    get_product_listings
    get_feedbacks
    get_my_info
}

# Helper functions to get user IDs from database
get_seller_id() {
    local num=$1
    export PGPASSWORD="$DB_PASSWORD"
    local id=$(psql -h "$DB_HOST" -p "$DB_PORT" -U "$DB_USER" -d "$DB_NAME" -tAc "SELECT id FROM users WHERE email = 'seller${num}@example.com';" 2>/dev/null)
    unset PGPASSWORD
    echo "$id"
}

get_buyer_id() {
    local num=$1
    export PGPASSWORD="$DB_PASSWORD"
    local id=$(psql -h "$DB_HOST" -p "$DB_PORT" -U "$DB_USER" -d "$DB_NAME" -tAc "SELECT id FROM users WHERE email = 'buyer${num}@example.com';" 2>/dev/null)
    unset PGPASSWORD
    echo "$id"
}

# =============================================================================
# Script Entry Point
# =============================================================================

# Check if jq is installed for pretty JSON output
if ! command -v jq &> /dev/null; then
    print_info "Note: Install 'jq' for pretty JSON output"
fi

# Parse command line arguments
case "$1" in
    --create-all)
        run_all_create_tests
        ;;
    --get-all)
        run_all_get_tests
        ;;
    --categories)
        create_categories
        ;;
    --products)
        create_products
        ;;
    --listings)
        create_product_listings
        ;;
    --feedbacks)
        create_feedbacks
        ;;
    --users)
        create_users
        ;;
    --drop-all)
        drop_all_force
        ;;
    --help|-h)
        echo "Usage: $0 [OPTIONS]"
        echo ""
        echo "Options:"
        echo "  --create-all    Run all create tests (users, categories, products, listings, feedbacks)"
        echo "  --get-all       Run all get tests"
        echo "  --users         Create buyers and sellers only"
        echo "  --categories    Create categories only"
        echo "  --products      Create products only"
        echo "  --listings      Create product listings only"
        echo "  --feedbacks     Create feedbacks only"
        echo "  --drop-all      Drop all data from database (no confirmation)"
        echo "  --help, -h      Show this help"
        echo ""
        echo "Environment Variables:"
        echo "  BASE_URL        API base URL (default: http://localhost:8003)"
        echo "  AUTH_TOKEN      Bearer token for authenticated endpoints"
        echo "  DB_HOST         Database host (default: localhost)"
        echo "  DB_PORT         Database port (default: 5432)"
        echo "  DB_NAME         Database name (default: ecommerce)"
        echo "  DB_USER         Database user (default: admin)"
        echo "  DB_PASSWORD     Database password (default: admin)"
        echo ""
        echo "Example:"
        echo "  ./test.sh --create-all"
        echo "  ./test.sh --drop-all && ./test.sh --create-all"
        echo "  DB_HOST=localhost DB_PORT=5432 ./test.sh --users"
        ;;
    *)
        # Interactive mode
        while true; do
            show_menu
            read -p "Select option: " choice
            case $choice in
                1) create_users ;;
                2) get_users ;;
                3) create_categories ;;
                4) get_categories ;;
                5) create_products ;;
                6) get_products ;;
                7) create_product_listings ;;
                8) get_product_listings ;;
                9) create_feedbacks ;;
                10) get_feedbacks ;;
                11) get_my_info ;;
                12) update_first_login_info ;;
                13) run_all_create_tests ;;
                14) run_all_get_tests ;;
                15) drop_all ;;
                0) echo "Goodbye!"; exit 0 ;;
                *) print_error "Invalid option" ;;
            esac
        done
        ;;
esac

echo -e "\n${GREEN}Done!${NC}"
