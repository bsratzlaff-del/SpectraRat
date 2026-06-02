# Private Key login
ssh -i "C:\Users\bsrat\.ssh\SpectraRatServer_key.pem" azureuser@158.23.58.74

# Go to Project Folder
cd spectrarat


#  Verify Docker is running
docker -v

#take down old environment
docker-compose down

#Build new environment
docker-compose up -d --build

# Show the running containers to confirm
docker ps
