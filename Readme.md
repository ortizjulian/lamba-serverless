1. Crear un bucket y subir los archivos de las lambdas
2. Desplegar stack: aws cloudformation create-stack --stack-name retoaws --template-body file://stack-personas-v1.yaml --capabilities CAPABILITY_IAM
3. Realizar cambios : aws cloudformation update-stack --stack-name retoaws --template-body file://stack-personas-v1.yaml --capabilities CAPABILITY_IAM
