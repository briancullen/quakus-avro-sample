curl --header "Content-Type: application/json" \
  --request POST \
  --data '{"title":"The Shawshank Redemption","year":1994}' \
  http://localhost:8081/movies

curl --header "Content-Type: application/json" \
  --request POST \
  --data '{"title":"The Godfather","year":1972}' \
  http://localhost:8081/movies

curl --header "Content-Type: application/json" \
  --request POST \
  --data '{"title":"The Dark Knight","year":2008}' \
  http://localhost:8081/movies

curl --header "Content-Type: application/json" \
  --request POST \
  --data '{"title":"12 Angry Men","year":1957}' \
  http://localhost:8081/movies