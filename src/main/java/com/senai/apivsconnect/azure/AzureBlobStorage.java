package com.senai.apivsconnect.azure;

// Include the following imports to use blob APIs.
import com.microsoft.azure.storage.*;
import com.microsoft.azure.storage.blob.*;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class AzureBlobStorage {

    public static final String storageConnectionString = "Conexao do Azure";


    // Criando função para enviar arquivo
    public String UploadFileToBlob(MultipartFile arquivo, String nomeArquivo) {
        try {
            // Acessando os recursos da conta por meio da connection string
            CloudStorageAccount storageAccount = CloudStorageAccount.parse(storageConnectionString);

            // Acessando os dados de conexao com o blob
            CloudBlobClient blobClient = storageAccount.createCloudBlobClient();

            // Reconhecendo o container criado
            CloudBlobContainer container = blobClient.getContainerReference("container do blob");

            // Criando uma referencia do novo arquivo que será gerado
            CloudBlockBlob blob = container.getBlockBlobReference(nomeArquivo);

            blob.upload(arquivo.getInputStream(), arquivo.getSize());

            return "Imagem enviada com sucesso";


        } catch (Exception e) {
            // Output the stack trace.
            e.printStackTrace();
        }

        return "Imagem não enviada";
    }
}
