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
package ro.emilianbold.jretina.impl;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import javax.imageio.ImageIO;
import ro.emilianbold.jretina.RetinaToolkit;
import ro.emilianbold.jretina.spi.ImageBundle;
import ro.emilianbold.jretina.spi.ImageBundleFactory;

/**
 * A plain image bundle factory that loads both image variants from the start.
 * 
 * <p>
 * Do not instantiate this class unless certain. {@link RetinaToolkit#createIcon(java.net.URL)} will
 * automatically use a factory instance.
 */
public class PlainImageBundleFactory implements ImageBundleFactory {

    private URL createRetinaURL(URL original) throws MalformedURLException {
        final String retinaURL;
        String surl = original.toExternalForm();
        int dot = surl.lastIndexOf('.'); //NOI18N
        if (dot == -1) {
            retinaURL = surl + "@2x"; //NOI18N
        } else {
            retinaURL = surl.substring(0, dot) + "@2x" + surl.substring(dot); //NOI18N
        }

        return new URL(retinaURL);
    }

    @Override
    public ImageBundle create(URL url) throws IOException {
        final BufferedImage plainImage = ImageIO.read(url);

        BufferedImage rimg;
        try {
            rimg = ImageIO.read(createRetinaURL(url));
        } catch (IOException ioe) {
            rimg = null;
        }

        final BufferedImage retinaImage = rimg;
        
        return new ImageBundle() {
            @Override
            public Image getRetinaImage() {
                return retinaImage;
            }

            @Override
            public Image getPlainImage() {
                return plainImage;
            }

            @Override
            public int getWidth() {
                return plainImage.getWidth();
            }

            @Override
            public int getHeight() {
                return plainImage.getHeight();
            }
        };
    }
};
