package info.theros.rsync4j.checksums;

import info.theros.rsync4j.util.Rsync4jException;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.zip.Adler32;

import org.apache.commons.codec.binary.Hex;

public class BlockChecksums {
	private long offset;
	private long size;
	private long weakChecksum;
	private byte[] strongChecksum;

	public BlockChecksums(byte[] buf, long offset, long size) {
		this.offset = offset;
		this.size = size;
		this.weakChecksum = generateWeakChecksum(buf);
		this.strongChecksum = generateStrongChecksum(buf);
	}

	private byte[] generateStrongChecksum(byte[] buf) {
		try {
			MessageDigest messageDigest = MessageDigest.getInstance("MD5");
			messageDigest.update(buf);
			return messageDigest.digest();
		} catch (NoSuchAlgorithmException e) {
			throw new Rsync4jException(e);
		}
	}

	private long generateWeakChecksum(byte[] buf) {
		Adler32 adler32 = new Adler32();
		adler32.update(buf);
		return adler32.getValue();
	}

	public long getOffset() {
		return offset;
	}

	public long getSize() {
		return size;
	}

	public long getWeakChecksum() {
		return weakChecksum;
	}

	public byte[] getStrongChecksum() {
		return strongChecksum;
	}

	public String getHexStrongChecksum() {
		return new String(Hex.encodeHex(strongChecksum));
	}

	@Override
	public String toString() {
		return "offset: " + offset + " size: " + size + " weak sum: "
				+ weakChecksum + " strong sum: " + getHexStrongChecksum();
	}
}
