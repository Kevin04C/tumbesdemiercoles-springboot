package com.tumbesdemiercoles.api.utils;

/**
 * Clase utilitaria que contiene las URLs por defecto utilizadas para imágenes
 * del sistema.
 *
 * <p>Estas URLs se emplean cuando un usuario o un curso no tiene una imagen
 * personalizada asignada. Generalmente son recursos alojados en un servicio
 * externo como Cloudinary.</p>
 *
 * <p>La clase no debe ser instanciada, ya que únicamente proporciona constantes.</p>
 */
public class ImageUrlDefault {

  public static final String defaultUserImage = "http://res.cloudinary.com/dt86tk7ed/image/upload/v1758151082/curso-digital/users/anonimo_m9l9vc.jpg";
  public static final String defaultCourseImage = "https://res.cloudinary.com/dt86tk7ed/image/upload/v1758316648/curso-digital/course/defaultcourse_ydsokv.jpg";


}
