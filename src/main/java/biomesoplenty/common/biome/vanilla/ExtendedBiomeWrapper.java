package biomesoplenty.common.biome.vanilla;

import java.util.HashMap;
import java.util.Map;

import biomesoplenty.api.biome.BiomeOwner;
import biomesoplenty.api.biome.IExtendedBiome;
import biomesoplenty.api.config.IBOPWorldSettings;
import biomesoplenty.api.config.IConfigObj;
import biomesoplenty.api.enums.BOPClimates;
import biomesoplenty.api.generation.GeneratorStage;
import biomesoplenty.api.generation.IGenerator;
import biomesoplenty.common.enums.BOPPlants;
import biomesoplenty.common.util.biome.BiomeUtils;
import biomesoplenty.common.world.GenerationManager;
import biomesoplenty.common.world.generator.GeneratorFlora;
import net.minecraft.init.Biomes;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.biome.Biome;

public class ExtendedBiomeWrapper implements IExtendedBiome
{
    public final Biome biome;
    private GenerationManager generationManager = new GenerationManager();
    private Map<BOPClimates, Integer> weightMap = new HashMap<BOPClimates, Integer>();
    
    public ResourceLocation beachBiomeLocation = BiomeUtils.getLocForBiome(Biomes.BEACH);
    
    public ExtendedBiomeWrapper(Biome biome)
    {
        this.biome = biome;
        
        // roots
        this.addGenerator("roots", GeneratorStage.FLOWERS,(new GeneratorFlora.Builder()).amountPerChunk(4.0F).with(BOPPlants.ROOT).create());
    }

    @Override
    public void applySettings(IBOPWorldSettings settings){}
    
    @Override
    public void configure(IConfigObj conf) 
    {
        this.beachBiomeLocation = conf.getResourceLocation("beachBiomeLocation", this.beachBiomeLocation);
        
        // Allow generators to be configured
        IConfigObj confGenerators = conf.getObject("generators");
        if (confGenerators != null)
        {
            for (String name : confGenerators.getKeys())
            {
                this.generationManager.configureWith(name, confGenerators.getObject(name));
            }
        }
    }
    
    @Override
    public BiomeOwner getBiomeOwner()
    {
        return BiomeOwner.OTHER;
    }

    @Override
    public void addGenerator(String name, GeneratorStage stage, IGenerator generator)
    {
        this.generationManager.addGenerator(name, stage, generator);
    }
    
    public IGenerator getGenerator(String name)
    {
    	return this.generationManager.getGenerator(name);
    }
    
    public void removeGenerator(String name)
    {
        this.generationManager.removeGenerator(name);
    }
    
    @Override
    public GenerationManager getGenerationManager()
    {
        return this.generationManager;
    }
    
    @Override
    public Map<BOPClimates, Integer> getWeightMap()
    {
        return this.weightMap;
    }
    
    @Override
    public void addWeight(BOPClimates climate, int weight)
    {
        this.weightMap.put(climate, weight);
    }
    
    @Override
    public void clearWeights()
    {
        this.weightMap.clear();
    }
    
    @Override
    public ResourceLocation getBeachLocation() 
    {
        return this.beachBiomeLocation;
    }
    
    @Override
    public ResourceLocation getResourceLocation() 
    {
        return BiomeUtils.getLocForBiome(this.biome);
    }

    @Override
    public Biome getBaseBiome() 
    {
        return this.biome;
    }
}
