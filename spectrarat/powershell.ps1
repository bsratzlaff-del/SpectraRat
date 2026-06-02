#Sends Dockerfile to server
scp -r -i "C:\Users\bsrat\.ssh\SpectraRatServer_key.pem" "C:\Users\bsrat\Code\Capstone\Task 3\d424-software-engineering-capstone" azureuser@158.23.58.74:/home/azureuser/

# Log into server
ssh -i "C:\Users\bsrat\.ssh\SpectraRatServer_key.pem" azureuser@158.23.58.74