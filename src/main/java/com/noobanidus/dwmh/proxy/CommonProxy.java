package com.noobanidus.dwmh.proxy;

import com.google.common.collect.Lists;
import com.noobanidus.dwmh.DWMH;
import com.noobanidus.dwmh.config.CreativeTabDWMH;
import com.noobanidus.dwmh.config.DWMHConfig;
import com.noobanidus.dwmh.config.Registrar;
import com.noobanidus.dwmh.proxy.steeds.DummySteedProxy;
import com.noobanidus.dwmh.proxy.steeds.ISteedProxy;
import com.noobanidus.dwmh.proxy.steeds.SteedProxy;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.ModContainer;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLLoadCompleteEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

import java.util.Optional;

public class CommonProxy implements ISidedProxy {
    public void preInit(FMLPreInitializationEvent event) {
        DWMH.TAB = new CreativeTabDWMH(CreativeTabs.getNextID(), DWMH.MODID);
        Registrar.preInit();
        DWMH.steedProxy = new SteedProxy();
    }

    public void init(FMLInitializationEvent e) {
    }

    @SuppressWarnings("unchecked")
    public void postInit(FMLPostInitializationEvent e) {
        if (DWMHConfig.proxies.enable.animania) {
            DWMH.animaniaProxy = ((Optional<ISteedProxy>) e.buildSoftDependProxy("animania", "com.noobanidus.dwmh.proxy.steeds.AnimaniaProxy")).orElse(new DummySteedProxy());
        }
        if (DWMHConfig.proxies.enable.mocreatures) {
            DWMH.mocProxy = ((Optional<ISteedProxy>) e.buildSoftDependProxy("mocreatures", "com.noobanidus.dwmh.proxy.steeds.MOCProxy")).orElse(new DummySteedProxy());
            if (Loader.isModLoaded("mocreatures")) {
                MinecraftForge.EVENT_BUS.register(DWMH.mocProxy.getClass());
            }
        }
        if (DWMHConfig.proxies.enable.zawa) {
            DWMH.zawaProxy = ((Optional<ISteedProxy>) e.buildSoftDependProxy("zawa", "com.noobanidus.dwmh.proxy.steeds.ZawaProxy")).orElse(new DummySteedProxy());
            if (Loader.isModLoaded("zawa")) {
                ModContainer zawa = Loader.instance().getIndexedModList().get("zawa");
                if (!zawa.getVersion().equals("1.12.2-1.4.0")) {
                    DWMH.zawaProxy = new DummySteedProxy();
                    DWMH.LOG.error("ZAWA is only supported for version 1.4.0. ZAWA compatibility has been disabled");
                }
            }
        }
        if (DWMHConfig.proxies.enable.ultimate_unicorn_mod) {
            DWMH.unicornProxy = ((Optional<ISteedProxy>) e.buildSoftDependProxy("ultimate_unicorn_mod", "com.noobanidus.dwmh.proxy.steeds.UnicornProxy")).orElse(new DummySteedProxy());
            if (Loader.isModLoaded("ultimate_unicorn_mod")) {
                MinecraftForge.EVENT_BUS.register(DWMH.unicornProxy.getClass());
            }
        }

        DWMH.proxyList = Lists.newArrayList(DWMH.animaniaProxy, DWMH.mocProxy, DWMH.zawaProxy, DWMH.unicornProxy, DWMH.vanillaProxy);
        DWMH.proxyList.removeIf(i -> !i.isLoaded());
        DWMH.resolveClasses();
    }

    public void loadComplete(FMLLoadCompleteEvent event) {
    }
}