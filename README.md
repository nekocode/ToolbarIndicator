# ToolbarIndicator
[![Apache 2.0 License](https://img.shields.io/badge/license-Apache%202.0-blue.svg?style=flat)](http://www.apache.org/licenses/LICENSE-2.0.html) [![Release](https://img.shields.io/github/release/nekocode/ToolbarIndicator.svg?label=Jitpack)](https://jitpack.io/#nekocode/ToolbarIndicator)  

Inspired from Twitter iOS App and modified from [CircleIndicator](https://github.com/ongakuer/CircleIndicator).

### Preview
![preview](art/preview.gif)

### Using with gradle
- Add the JitPack repository to your root build.gradle:
```gradle
repositories {
    maven { url "https://jitpack.io" }
}
```

- Add the dependency to your sub build.gradle:
```gradle
dependencies {
    compile 'com.github.nekocode:ToolbarIndicator:v1.0'
}
```

- Set the target ViewPager for ToolbarIndicator:
```
ToolbarIndicator toolbarIndicator = (ToolbarIndicator) this.findViewById(R.id.indicator);
toolbarIndicator.setViewPager(viewPager);
```

### Sample
ToolbarIndicator get titles from FragmentPagerAdapter's getPageTitle function, check the **[sample project](sample)** for more detail.
