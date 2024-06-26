package com.notjang.savor.entity;

import com.google.gson.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.registries.ForgeRegistries;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class CrackerRecipeOverwrite {
    public final Item item;
    public final List<ItemStack> results;

    public CrackerRecipeOverwrite(Item item, List<ItemStack> results) {
        this.item = item;
        this.results = results;
    }

    CrackerRecipeOverwrite setRegistryName(ResourceLocation registryName) {
        return this;
    }

    public static class Deserializer implements JsonDeserializer<CrackerRecipeOverwrite> {
        @Override
        public CrackerRecipeOverwrite deserialize(JsonElement json, Type type, JsonDeserializationContext context) throws JsonParseException {
            JsonObject jsonObject = json.getAsJsonObject();
            Item item = jsonObject.has("item") && ForgeRegistries.ITEMS.containsKey(new ResourceLocation(jsonObject.get("item").getAsString())) ? ForgeRegistries.ITEMS.getValue(new ResourceLocation(jsonObject.get("item").getAsString())) : null;
            List<ItemStack> results = new ArrayList<>();
            if (jsonObject.has("results")) {
                jsonObject.get("results").getAsJsonArray().forEach((result) -> {
                    Item resultItemType = result.getAsJsonObject().has("item") && ForgeRegistries.ITEMS.containsKey(new ResourceLocation(result.getAsJsonObject().get("item").getAsString())) ? ForgeRegistries.ITEMS.getValue(new ResourceLocation(result.getAsJsonObject().get("item").getAsString())) : null;
                    int resultItemCount = result.getAsJsonObject().has("count") ? result.getAsJsonObject().get("count").getAsInt() : 1;
                    ItemStack resultItem = new ItemStack(resultItemType, resultItemCount);
                    results.add(resultItem);
                });
            }
            return new CrackerRecipeOverwrite(item, results);
        }
    }
}
