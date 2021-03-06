package com.noobanidus.dwmh.init;

import com.noobanidus.dwmh.DWMH;
import com.noobanidus.dwmh.items.ItemOcarina;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.registries.IForgeRegistry;

import java.util.Objects;

@Mod.EventBusSubscriber(modid = DWMH.MODID)
public class ItemRegistry {
  public static ItemOcarina OCARINA = (ItemOcarina) new ItemOcarina().setTranslationKey("dwmh.ocarina").setRegistryName(new ResourceLocation(DWMH.MODID, "ocarina")).setCreativeTab(DWMH.TAB);

  @SubscribeEvent
  public static void onItemRegister(RegistryEvent.Register<Item> event) {
    IForgeRegistry<Item> registry = event.getRegistry();
    registry.register(OCARINA);
  }

  @SubscribeEvent
  @SideOnly(Side.CLIENT)
  public static void onModelRegister(ModelRegistryEvent event) {
    ModelLoader.setCustomModelResourceLocation(OCARINA, 0, new ModelResourceLocation(Objects.requireNonNull(OCARINA.getRegistryName()), "inventory"));
  }
}
