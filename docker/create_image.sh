# Move to angular folder
cd frontend

# Create production build
ng build --configuration production

# Move to docker folder
cd docker

# Build container
docker build -t blasetvrtumi/bookmarks -f Dockerfile ../

# Push image
docker push blasetvrtumi/bookmarks

# Up compose
docker-compose -p bookmarks up