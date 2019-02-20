package io.bw.useful.util;

import static org.assertj.core.api.Assertions.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Collectors;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

/**
 * @author Byungwook lee on 2019-02-20
 * quddnr153@gmail.com
 * https://github.com/quddnr153
 */
@RunWith(MockitoJUnitRunner.class)
public class CompressionUtilTest {
	@Rule
	public TemporaryFolder folder = new TemporaryFolder();

	@InjectMocks
	private CompressionUtil compressionUtil;

	@Test
	public void decompress_Success() throws IOException {
		// Given
		Resource zipResource = new ClassPathResource("compression/compression-file.zip");
		Path source = Path.of(zipResource.getFile().getAbsolutePath());
		Path targetDirectory = Path.of(folder.getRoot().getAbsolutePath());

		// When
		Path compressedDirectory = compressionUtil.decompress(source, targetDirectory);

		// Then
		assertThat(compressedDirectory).exists();

		List<Path> compressedFiles = Files.walk(compressedDirectory)
			.filter(path -> !path.equals(compressedDirectory))
			.collect(Collectors.toList());
		assertThat(compressedFiles).hasSize(5);
	}

	@Test(expected = IllegalStateException.class)
	public void decompress_Not_Compressed_File() throws IOException {
		// Given
		Resource zipResource = new ClassPathResource("compression/not-compressed-file.txt");
		Path source = Path.of(zipResource.getFile().getAbsolutePath());
		Path targetDirectory = Path.of(folder.getRoot().getAbsolutePath());

		// When and Throw Exception
		compressionUtil.decompress(source, targetDirectory);
	}

	@Test(expected = IllegalArgumentException.class)
	public void decompress_Access_Denied_Target() throws IOException {
		// Given
		Resource zipResource = new ClassPathResource("compression/compression-file.zip");
		Path source = Path.of(zipResource.getFile().getAbsolutePath());
		Path targetDirectory = Path.of("/");

		// When and Throw Exception
		compressionUtil.decompress(source, targetDirectory);
	}
}
