package org.apache.con.todos;

import java.io.File;

import org.apache.con.todos.domain.InitialData;
import org.apache.con.todos.domain.Todo;
import org.qi4j.api.common.Visibility;
import org.qi4j.bootstrap.ApplicationAssembler;
import org.qi4j.bootstrap.ApplicationAssembly;
import org.qi4j.bootstrap.ApplicationAssemblyFactory;
import org.qi4j.bootstrap.AssemblyException;
import org.qi4j.bootstrap.LayerAssembly;
import org.qi4j.bootstrap.ModuleAssembly;
import org.qi4j.entitystore.file.assembly.FileEntityStoreAssembler;
import org.qi4j.entitystore.leveldb.LevelDBEntityStoreAssembler;
import org.qi4j.entitystore.memory.MemoryEntityStoreAssembler;
import org.qi4j.index.elasticsearch.assembly.ESFilesystemIndexQueryAssembler;
import org.qi4j.index.rdf.assembly.RdfNativeSesameStoreAssembler;
import org.qi4j.library.fileconfig.FileConfigurationAssembler;
import org.qi4j.library.fileconfig.FileConfigurationOverride;
import org.qi4j.library.rdf.repository.NativeConfiguration;
import org.qi4j.valueserialization.jackson.JacksonValueSerializationAssembler;

public class TodosApplicationAssembler
    implements ApplicationAssembler
{
    public static final String LAYER_CONFIG = "Configuration Layer";
    public static final String LAYER_STORAGE = "Storage Layer";
    public static final String LAYER_DOMAIN = "Domain Layer";
    public static final String LAYER_CONNECTIVITY = "Connectivity Layer";

    public static final String MODULE_CONFIGS = "Configurations Module";
    public static final String MODULE_REST = "REST Module";

    @Override
    public ApplicationAssembly assemble( ApplicationAssemblyFactory applicationFactory )
        throws AssemblyException
    {
        ApplicationAssembly assembly = applicationFactory.newApplicationAssembly();

        LayerAssembly configLayer = assembly.layer( LAYER_CONFIG );
        LayerAssembly storageLayer = assembly.layer( LAYER_STORAGE );
        LayerAssembly domainLayer = assembly.layer( LAYER_DOMAIN );
        LayerAssembly connectivityLayer = assembly.layer( LAYER_CONNECTIVITY );

        ModuleAssembly configModule = assembleConfiguration( configLayer );
        assembleStorage( storageLayer, configModule );
        assembleDomain( domainLayer );
        assembleConnectivity( connectivityLayer );

        storageLayer.uses( configLayer );
        domainLayer.uses( storageLayer );
        connectivityLayer.uses( domainLayer, storageLayer );

        return assembly;
    }

    private ModuleAssembly assembleConfiguration( LayerAssembly configLayer )
        throws AssemblyException
    {
        // Configuration Services
        ModuleAssembly configServices = configLayer.module( "Configuration Services Module" );
        File baseDir = new File( "build/zest-files" );
        baseDir.mkdirs();
        FileConfigurationOverride fileConfigOverride = new FileConfigurationOverride()
            .withCache( new File( baseDir, "cache" ) )
            .withConfiguration( new File( baseDir, "conf" ) )
            .withData( new File( baseDir, "data" ) )
            .withLog( new File( baseDir, "logs" ) )
            .withTemporary( new File( baseDir, "temp" ) );
        new FileConfigurationAssembler()
            .withOverride( fileConfigOverride )
            .visibleIn( Visibility.application )
            .assemble( configServices );
        new JacksonValueSerializationAssembler()
            .visibleIn( Visibility.layer )
            .assemble( configServices );
        new MemoryEntityStoreAssembler()
            .visibleIn( Visibility.layer )
            .assemble( configServices );
        // Configurations Entities Module
        // This module will contains configurations entities for services residing in other layers
        return configLayer.module( "Configurations Module" );
    }

    private void assembleStorage( LayerAssembly storageLayer, ModuleAssembly configModule )
        throws AssemblyException
    {
        // Serialization
        new JacksonValueSerializationAssembler()
            .visibleIn( Visibility.layer )
            .assemble( storageLayer.module( "Serialization Module" ) );
        // EntityStore
        new FileEntityStoreAssembler()
            .identifiedBy( "file-entitystore" )
            .visibleIn( Visibility.application )
            .withConfig( configModule, Visibility.application )
            .assemble( storageLayer.module( "EntityStore Module" ) );
//        new LevelDBEntityStoreAssembler()
//            .identifiedBy( "leveldb-entitystore")
//            .visibleIn( Visibility.application )
//            .withConfig( configModule, Visibility.application )
//            .assemble( storageLayer.module( "EntityStore Module" ) );
        // Index/Query
        new RdfNativeSesameStoreAssembler( Visibility.application, Visibility.application )
            .assemble( storageLayer.module( "Index/Query Module" ) );
        configModule.entities( NativeConfiguration.class ).visibleIn( Visibility.application );
//        new ESFilesystemIndexQueryAssembler()
//            .identifiedBy( "elasticsearch-indexing")
//            .visibleIn( Visibility.application )
//            .withConfig( configModule, Visibility.application )
//            .assemble( storageLayer.module( "Index/Query Module" ) );
    }

    private void assembleDomain( LayerAssembly domainLayer )
    {
        ModuleAssembly domainModule = domainLayer.module( "Domain Module" );
        domainModule.entities( Todo.class ).visibleIn( Visibility.application );
        domainModule.values( Todo.class ).visibleIn( Visibility.application );
        domainModule.services( InitialData.class ).instantiateOnStartup();
    }

    private void assembleConnectivity( LayerAssembly connectivityLayer )
        throws AssemblyException
    {
        new JacksonValueSerializationAssembler().assemble( connectivityLayer.module( MODULE_REST ) );
    }
}
