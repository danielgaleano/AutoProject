/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sistem.proyecto.web.controller;

import com.sistem.proyecto.entity.Empresa;
import com.sistem.proyecto.entity.Imagen;
import com.sistem.proyecto.userDetail.UserDetail;
import java.awt.AlphaComposite;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import javax.imageio.ImageIO;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 *
 * @author Miguel
 */
@Controller
public class ImagenController extends BaseController{
    
    @ResponseBody
    @RequestMapping(value = "/obtenerImagen/{entidad}/{entidadId}", method = RequestMethod.GET)
    public void descagarImagen(@PathVariable("entidad") String entidad, @PathVariable("entidadId") Long entidadId,
                    HttpServletRequest request, HttpServletResponse response) {

            UserDetail userDetail = ((UserDetail) SecurityContextHolder.getContext().getAuthentication().getPrincipal());

            try {

                inicializarImagenManager();

                response.setHeader("Pragma", "no-cache");
                response.addHeader("Cache-Control", "no-cache");

                // Si es un agregar entidadId es 0

                if (entidadId != 0) { // Si es un editar
                    Imagen imagen = new Imagen();
                    imagen.setNombreTabla(entidad);
                    imagen.setEntidadId(entidadId);
                    imagen = imagenManager.get(imagen);

                    if (imagen != null) { // Si existe la imagen
                            File file = convertirImagen(imagen.getImagen(), 640);
                            BufferedImage bufferedImage = ImageIO.read(file);
                            OutputStream output = response.getOutputStream();
                            ImageIO.write(bufferedImage, "jpg", output);
                            output.flush();
                            output.close();
                    } else { // Si no existe la imagen
                            ServletContext context = request.getServletContext();
                            File downloadFile = new File(context.getRealPath("") + "/WEB-INF/resources/img/" + entidad + ".jpg");
                            FileInputStream input = new FileInputStream(downloadFile);
                            BufferedImage image = ImageIO.read(input);
                            OutputStream output = response.getOutputStream();
                            ImageIO.write(image, "jpg", output);
                            output.flush();
                            output.close();
                    }
                } else { // Si es un agregar
                    Imagen imagen = new Imagen();
                    imagen.setNombreTabla(entidad);
                    imagen.setEntidadId(0l);
                    imagen = imagenManager.get(imagen);

                    if (imagen != null) { // Si existe la imagen temporal
                            File file = convertirImagen(imagen.getImagen(), 640);
                            BufferedImage bufferedImage = ImageIO.read(file);
                            OutputStream output = response.getOutputStream();
                            ImageIO.write(bufferedImage, "jpg", output);
                            output.flush();
                            output.close();
                    } else { // Si no existe la imagen temporal
                            ServletContext context = request.getServletContext();
                            File downloadFile = new File(context.getRealPath("") + "/WEB-INF/resources/img/" + entidad + ".jpg");
                            FileInputStream input = new FileInputStream(downloadFile);
                            BufferedImage image = ImageIO.read(input);
                            OutputStream output = response.getOutputStream();
                            ImageIO.write(image, "jpg", output);
                            output.flush();
                            output.close();
                    }
                }
            } catch (Exception e) {
                    logger.error("descargar imagen", e);
            }
    }
    
    public static File convertirImagen(byte[] bytes, Integer width) {
		
        File file = null;

        try {
            Image image = ImageIO.read(new ByteArrayInputStream(bytes));

            int height;
            if (width != null && width < image.getWidth(null)) {
                    double aspectRatio = (double) image.getWidth(null) / (double) image.getHeight(null);
                    height = (int) (width / aspectRatio);
            } else {
                    width = image.getWidth(null);
                    height = image.getHeight(null);
            }

            final BufferedImage bufferedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
            final Graphics2D graphics2D = bufferedImage.createGraphics();
            graphics2D.setComposite(AlphaComposite.Src);
            graphics2D.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
            graphics2D.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
            graphics2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            graphics2D.drawImage(image, 0, 0, width, height, null);
            graphics2D.dispose();

            file = File.createTempFile("temp", ".tmp");
            ImageIO.write(bufferedImage, "jpg", file);

            return file;

        } catch (IOException ex) {
                logger.error("convertir imagen",ex);
                return null;
        } finally{
                file.deleteOnExit();
        }
	}
    
}
