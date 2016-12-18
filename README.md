# jretina

Load Retina icons in Java

```java
RetinaToolkit.getDefault().createIcon(getClass().getResource("logo.png"))
```

The icon will automatically detect the used display and switch from the retina to the
non-retina image. This will allow you to have the best image on screen.

The library is intentionally simple. There is no caching, lazy loading or any other
complex features. You can hook your existing caching infrastructure with the optional
ImageBundleFactory subclass.
