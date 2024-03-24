# Move to docker folder
cd docker

# Build container
docker build -t blasetvrtumi/bookmarks -f Dockerfile ../

# Push image
docker push blasetvrtumi/bookmarks

# Up compose
docker-compose -p bookmarks up