package org.example.demo;

import io.vertx.core.Handler;
import io.vertx.ext.web.FileUpload;
import io.vertx.ext.web.RoutingContext;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * @author Zero
 *         Created on 2017/6/6.
 */
public class UploadHandler implements Handler<RoutingContext> {

    @Override
    public void handle(RoutingContext context) {
        Set<FileUpload> fileUploads = context.fileUploads().stream().filter(fileUpload -> fileUpload.size() > 0).collect(Collectors.toSet());
        if (fileUploads.isEmpty()) {
            context.response().end("必须上传两个文件");
        } else {
            Optional<FileUpload> firstFile = fileUploads.stream().filter(f -> f.name().equals("firstFile")).findFirst();
            Optional<FileUpload> secondFile = fileUploads.stream().filter(f -> f.name().equals("secondFile")).findFirst();
            if (!firstFile.isPresent() || !secondFile.isPresent()) {
                context.fail(400);
            } else {
                try {

                    FileUpload fileUpload = firstFile.get();
                    fileUpload.name();//表单数据的name属性
                    fileUpload.fileName();//原始文件名
                    //上传成功后在服务器上的名字,根据BodyHandler中的设置得来
                    //可以通过 new File(fileUpload.uploadedFileName()) 或者Paths.get(fileUpload.uploadedFileName()) 获取
                    fileUpload.uploadedFileName();

                    ////////////////

                    Path out = Files.createTempFile("merge-", ".out");
                    Path first = Paths.get(firstFile.get().uploadedFileName());
                    Paths.get(secondFile.get().uploadedFileName());
                    //合并成功后保存文件
                    String fid = "bucket/" + UUID.randomUUID().toString();
                    Path savePath = Paths.get("$Config.STORAGE_DIR", fid);
                    if (!Files.exists(savePath.getParent())) {
                        Files.createDirectories(savePath.getParent());
                    }
                    //模拟存储
                    Files.move(out, savePath);
                } catch (IOException e) {
                    context.response().end(e.getMessage());
                }
            }
        }
    }

}
