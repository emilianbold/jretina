/*
 * Copyright 2016 Emilian Marius Bold
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package ro.emilianbold.jretina;

import ro.emilianbold.jretina.impl.PlainImageBundleFactory;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Image;
import java.io.IOException;
import java.lang.reflect.Field;
import java.net.URL;
import javax.swing.Icon;
import ro.emilianbold.jretina.spi.ImageBundle;
import ro.emilianbold.jretina.spi.ImageBundleFactory;

/**
 * Load Retina icons or query the environment about Retina capabilities. 
 */
public final class RetinaToolkit {

    private final static RetinaToolkit INSTANCE = new RetinaToolkit();

    private final static ImageBundleFactory DEFAULT_FACTORY = new PlainImageBundleFactory();

    public static RetinaToolkit getDefault() {
        return INSTANCE;
    }

    private RetinaToolkit() {
        //nothing
    }

    /**
     * @return true if this machine has any retina display
     */
    public static boolean isRetina() {
        GraphicsEnvironment environment = GraphicsEnvironment.getLocalGraphicsEnvironment();

        for (GraphicsDevice screen : environment.getScreenDevices()) {
            if (isRetina(screen)) {
                return true;
            }
        }

        return false;
    }

    /**
     * @param device
     * @return true if the device is a retina device
     */
    public static boolean isRetina(GraphicsDevice device) {
        try {
            Field field = device.getClass().getDeclaredField("scale"); //NOI18N
            field.setAccessible(true);
            Object scale = field.get(device);
            if (scale instanceof Integer && (Integer) scale == 2) {
                return true;
            }
        } catch (IllegalAccessException | IllegalArgumentException | NoSuchFieldException | SecurityException e) {
        }
        return false;
    }

    /**
     * 
     * @param g a valid {@link Graphics} instance
     * @return true if the device associated with the {@link Graphics} argument is retina
     */
    public static boolean isRetina(Graphics g) {
        if (g instanceof Graphics2D) {
            Graphics2D g2 = (Graphics2D) g;

            GraphicsDevice device = g2.getDeviceConfiguration().getDevice();

            return isRetina(device);
        }
        return false;
    }

    /**
     * Create retina icon.
     * 
     * @param url the source URL for the plain image
     * @return a retina icon
     * @throws IOException if loading fails
     */
    public Icon createIcon(URL url) throws IOException {
        return createIcon(url, DEFAULT_FACTORY);
    }

    /**
     * Create retina icon using a specific image bundle factory.
     * 
     * <p>
     * This method is only recommended if you have your own {@link ImageBundleFactory}.
     * If you are unsure, just use the plain {@link #createIcon(java.net.URL)} method which
     * will use the default factory.
     * 
     * @param url the source URL for the plain image
     * @param factory the factory responsible with the actual image loading (and perhaps caching)
     * @return a retina icon
     * @throws IOException if loading fails
     */
    public Icon createIcon(URL url, ImageBundleFactory factory) throws IOException {
        final ImageBundle images = factory.create(url);
        
        return new Icon() {
            @Override
            public int getIconWidth() {
                return images.getWidth();
            }

            @Override
            public int getIconHeight() {
                return images.getHeight();
            }

            private Image getRetinaImage() {
                return images.getRetinaImage();
            }

            private Image getPlainImage() {
                return images.getPlainImage();
            }

            private boolean paintRetinaImage(Component c, Graphics g, int x, int y) {
                Image retina = getRetinaImage();

                if (retina == null) {
                    return false;
                }

                final Graphics2D g2 = (Graphics2D) g.create(x, y, getIconWidth(), getIconHeight());
                g2.scale(0.5, 0.5);
                g2.drawImage(retina, 0, 0, c);
                g2.dispose();

                return true;
            }

            private boolean paintPlainImage(Component c, Graphics g, int x, int y) {
                Image img = getPlainImage();

                if (img == null) {
                    return false;
                }

                g.drawImage(img, x, y, c);

                return true;
            }

            @Override
            public void paintIcon(Component c, Graphics g, int x, int y) {
                if (isRetina(g)) {
                    if (!paintRetinaImage(c, g, x, y)) {
                        paintPlainImage(c, g, x, y);
                    }
                } else {
                    //non retina screen
                    if (!paintPlainImage(c, g, x, y)) {
                        paintRetinaImage(c, g, x, y);
                    }
                }
            }

        };
    }

}
