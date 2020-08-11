package OnlineMusic.servlet;

import OnlineMusic.entity.User;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.util.List;


@WebServlet("/uploadMV")
public class UploadMVServlet extends HttpServlet {

    private final String SAVEPATH = "E:\\java练习\\200723\\web\\mv";

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("utf-8");
        resp.setContentType("application/json;charset=utf-8");

        User user = (User) req.getSession().getAttribute("user");
        if(user == null) {
            System.out.println("请登录后再上传MV！");
            resp.getWriter().write("<h2> 请登录后再上传MV"+"</h2>");
            return;
        }else {
            //上传
            FileItemFactory factory = new DiskFileItemFactory();
            ServletFileUpload upload = new ServletFileUpload(factory);
            List<FileItem> fileItems = null;

            try {
                fileItems = upload.parseRequest(req);
            } catch (FileUploadException e) {
                e.printStackTrace();
                return;
            }
            System.out.println("fileItems："+ fileItems);
            FileItem fileItem = fileItems.get(0);
            System.out.println("fileItem：" + fileItem);
            String fileName = fileItem.getName(); // 文件名
            req.getSession().setAttribute("fileName", fileName);
            try {
                fileItem.write(new File(SAVEPATH, fileName));
            } catch (Exception e) {
                e.printStackTrace();
                return;
            }
            //2、上传到数据库当中
            resp.sendRedirect("uploadsuccessMV.html");
        }
    }
}

