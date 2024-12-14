package com.notjang.savor.entity;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimpleJsonResourceReloadListener;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingRecipe;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CrackerLoader extends SimpleJsonResourceReloadListener {
    private static final Logger LOGGER = LogManager.getLogger();
    private static final Gson GSON = new GsonBuilder()
            .registerTypeAdapter(ResourceLocation.class, new ResourceLocation.Serializer())
            .registerTypeAdapter(Component.class, new Component.Serializer())
            .registerTypeAdapter(CrackerRecipeOverwrite.class, new CrackerRecipeOverwrite.Deserializer())
            .create();

    public static CrackerLoader instance;

    private final Map<ResourceLocation, CrackerRecipeOverwrite> recipeOverwrites;
    private final Map<Item, List<ItemStack>> cachedRecipes;

    public List<ItemStack> uncraftItem(Level level, Item item) {
        if (!cachedRecipes.containsKey(item)) {
            List<CraftingRecipe> recipes = level.getRecipeManager().getAllRecipesFor(RecipeType.CRAFTING);
            recipes = recipes.stream()
                    .filter(r -> r.getResultItem().is(item))
                    .filter(r -> r.getResultItem().getCount() == 1)
                    .toList();
            if (!recipes.isEmpty()) {
                CraftingRecipe recipe = recipes.get(0);
                cachedRecipes.put(item, recipe.getIngredients().stream().filter(i -> !i.isEmpty()).map(i -> i.getItems()[0].copy()).toList());
            }
            else {
                cachedRecipes.put(item, null);
            }
        }
        List<ItemStack> results = cachedRecipes.get(item);
        return (results != null) ? cachedRecipes.get(item).stream().map(ItemStack::copy).toList() : null;
    }

    public CrackerLoader() {
        super(GSON, "cracker");
        this.recipeOverwrites = new HashMap<>();
        this.cachedRecipes = new HashMap<>();
        instance = this;
    }

    @Override
    protected void apply(Map<ResourceLocation, JsonElement> object, @NotNull ResourceManager resourceManager, @NotNull ProfilerFiller profiler) {
        Map<ResourceLocation, CrackerRecipeOverwrite> parsedRecipeOverwrites = new HashMap<>();

        object.forEach((location, json) ->
        {
            ResourceLocation registryName = new ResourceLocation(location.getNamespace(), location.getPath());
            try {
                parsedRecipeOverwrites.put(registryName, GSON.fromJson(json, CrackerRecipeOverwrite.class).setRegistryName(registryName));
            } catch (Exception e) {
                LOGGER.error("Parsing error loading custom cracker recipe overwrite " + registryName, e);
            }
        });

        LOGGER.info("Loaded " + parsedRecipeOverwrites.size() + " Cracker Recipe Overwrites");

        this.recipeOverwrites.clear();
        this.recipeOverwrites.putAll(parsedRecipeOverwrites);

        this.cachedRecipes.clear();
        this.recipeOverwrites.forEach(((resource, overwrite) -> {
            cachedRecipes.put(overwrite.item(), overwrite.results());
            LOGGER.info(overwrite.item().toString() + " -> " + overwrite.results().toString());
        }));
    }
}
