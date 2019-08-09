package com.noobanidus.dwmh.init;

import com.noobanidus.dwmh.DWMH;
import com.noobanidus.dwmh.items.OcarinaItem;
import net.minecraft.client.renderer.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.IForgeRegistry;

import java.util.Objects;

@Mod.EventBusSubscriber(modid = DWMH.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ItemRegistry {
  public static OcarinaItem OCARINA = (OcarinaItem) new OcarinaItem().setRegistryName(new ResourceLocation(DWMH.MODID, "ocarina"));

  @SubscribeEvent
  public static void onItemRegister(RegistryEvent.Register<Item> event) {
    IForgeRegistry<Item> registry = event.getRegistry();
    registry.register(OCARINA);
  }

  /*@SubscribeEvent
  @OnlyIn(Dist.CLIENT)
  public static void onModelRegister(ModelRegistryEvent event) {
    ModelLoader.setCustomModelResourceLocation(OCARINA, 0, new ModelResourceLocation(Objects.requireNonNull(OCARINA.getRegistryName()), "inventory"));
  }*/
}
