#!/bin/bash
# Test Script for Library Service APIs
BASE_URL="http://localhost:8080"

echo "=============================================="
echo "  LIBRARY SERVICE - COMPREHENSIVE API TESTS"
echo "=============================================="

echo ""
echo "=== TEST 1: GET ALL BOOKS ==="
curl -s $BASE_URL/books

echo ""
echo ""
echo "=== TEST 2: SEARCH BOOKS (keyword=Tolkien) ==="
curl -s "$BASE_URL/books/search?keyword=Tolkien"

echo ""
echo ""
echo "=== TEST 3: GET BOOK BY ID (1) ==="
curl -s $BASE_URL/books/1

echo ""
echo ""
echo "=== TEST 4: ADD NEW BOOK ==="
curl -s -X POST $BASE_URL/books \
  -H 'Content-Type: application/json' \
  -d '{"title":"Clean Code","author":"Robert C. Martin"}'

echo ""
echo ""
echo "=== TEST 5: GET ALL MEMBERS ==="
curl -s $BASE_URL/members

echo ""
echo ""
echo "=== TEST 6: GET MEMBER BY ID (1) ==="
curl -s $BASE_URL/members/1

echo ""
echo ""
echo "=== TEST 7: ISSUE BOOK (Book 2 -> Member 1) ==="
curl -s -X POST $BASE_URL/issues/issue \
  -H 'Content-Type: application/json' \
  -d '{"bookId":2,"memberId":1}'

echo ""
echo ""
echo "=== TEST 8: ISSUE BOOK (Book 3 -> Member 1) ==="
curl -s -X POST $BASE_URL/issues/issue \
  -H 'Content-Type: application/json' \
  -d '{"bookId":3,"memberId":1}'

echo ""
echo ""
echo "=== TEST 9: ISSUE BOOK (Book 4 -> Member 1) ==="
curl -s -X POST $BASE_URL/issues/issue \
  -H 'Content-Type: application/json' \
  -d '{"bookId":4,"memberId":1}'

echo ""
echo ""
echo "=== TEST 10: ISSUE 4TH BOOK - SHOULD FAIL (MAX 3) ==="
curl -s -X POST $BASE_URL/issues/issue \
  -H 'Content-Type: application/json' \
  -d '{"bookId":5,"memberId":1}'

echo ""
echo ""
echo "=== TEST 11: ISSUE ALREADY-ISSUED BOOK - SHOULD FAIL ==="
curl -s -X POST $BASE_URL/issues/issue \
  -H 'Content-Type: application/json' \
  -d '{"bookId":2,"memberId":2}'

echo ""
echo ""
echo "=== TEST 12: GET AVAILABLE BOOKS (should be reduced) ==="
curl -s $BASE_URL/books/available

echo ""
echo ""
echo "=== TEST 13: GET MEMBER ISSUES (Member 1) ==="
curl -s $BASE_URL/members/1/issues

echo ""
echo ""
echo "=== TEST 14: GET ALL ACTIVE ISSUES ==="
curl -s $BASE_URL/issues/active

echo ""
echo ""
echo "=== TEST 15: RETURN BOOK (Issue ID 1) ==="
curl -s -X PUT $BASE_URL/issues/return/1

echo ""
echo ""
echo "=== TEST 16: RETURN ALREADY-RETURNED - SHOULD FAIL ==="
curl -s -X PUT $BASE_URL/issues/return/1

echo ""
echo ""
echo "=== TEST 17: AVAILABLE BOOKS (should include returned book) ==="
curl -s $BASE_URL/books/available

echo ""
echo ""
echo "=== TEST 18: VALIDATION - EMPTY BOOK TITLE ==="
curl -s -X POST $BASE_URL/books \
  -H 'Content-Type: application/json' \
  -d '{"title":"","author":"Test"}'

echo ""
echo ""
echo "=== TEST 19: VALIDATION - INVALID EMAIL ==="
curl -s -X POST $BASE_URL/members \
  -H 'Content-Type: application/json' \
  -d '{"name":"Test","email":"invalid-email"}'

echo ""
echo ""
echo "=== TEST 20: DUPLICATE EMAIL - SHOULD FAIL ==="
curl -s -X POST $BASE_URL/members \
  -H 'Content-Type: application/json' \
  -d '{"name":"Duplicate","email":"pritam@college.edu"}'

echo ""
echo ""
echo "=== TEST 21: NOT FOUND - INVALID BOOK ID ==="
curl -s $BASE_URL/books/999

echo ""
echo ""
echo "=== TEST 22: NOT FOUND - INVALID MEMBER ID ==="
curl -s $BASE_URL/members/999

echo ""
echo ""
echo "=============================================="
echo "  ALL TESTS COMPLETED!"
echo "=============================================="
