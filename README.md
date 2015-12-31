# ToolbatIndicator
[![Release](https://img.shields.io/github/release/nekocode/ToolbarIndicator.svg?label=Jitpack)](https://jitpack.io/#nekocode/ToolbarIndicator)
Insprite and modified from [CircleIndicator](https://github.com/ongakuer/CircleIndicator).

## Preview
![preview](art/preview.gif)

## How to use
Add the JitPack repository to your build.gradle:
```gradle
repositories {
    maven { url "https://jitpack.io" }
}
```

Add the dependency:
```gradle
dependencies {
    compile 'com.github.nekocode:ToolbarIndicator:v1.0'
}
```

Set the target ViewPager for ToolbarIndicator:
```
ToolbarIndicator toolbarIndicator = (ToolbarIndicator) this.findViewById(R.id.indicator);
toolbarIndicator.setViewPager(viewPager);
```

The ToolbarIndicator get titles from FragmentPagerAdapter's getPageTitle function, for more detail you can check about the **[sample project](https://github.com/nekocode/ToolbarIndicator/tree/master/sample)**.
