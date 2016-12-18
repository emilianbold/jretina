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
package ro.emilianbold.jretina.spi;

import java.io.IOException;
import java.net.URL;

/**
 * A factory for plain and retina images.
 * 
 * This factory exists to facilitate caching, lazy loading and other more complex
 * handling.
 */
public interface ImageBundleFactory {

    /**
     * Load the plain and retina image bundle.
     *
     * @param url the plain image URL
     * @return a non-null multi image
     * @throws IOException if loading fails
     */
    ImageBundle create(URL url) throws IOException;

}
